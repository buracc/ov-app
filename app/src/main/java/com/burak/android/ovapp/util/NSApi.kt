package com.burak.android.ovapp.util

import com.burak.android.ovapp.exception.StationNotFoundException
import com.burak.android.ovapp.model.stations.Station
import com.burak.android.ovapp.model.trips.Fare
import com.burak.android.ovapp.model.trips.Leg
import com.burak.android.ovapp.model.trips.Stop
import com.burak.android.ovapp.model.trips.Trip
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

object NSApi {

    private val stations = mutableMapOf<String, Station>()
    private val client = OkHttpClient()
    private val requestBase = Request.Builder()
        .addHeader("Ocp-Apim-Subscription-Key", Credentials.apiKey)

    suspend fun getStationByName(name: String): Station {
        return withContext(Dispatchers.IO) {
            getAllStations()[name] ?: throw StationNotFoundException(name)
        }
    }

    fun getAllStations(): Map<String, Station> {
        if (stations.isEmpty()) {
            val allStationsUrl =
                "https://gateway.apiportal.ns.nl/reisinformatie-api/api/v2/stations"
            val request = requestBase.url(allStationsUrl).build()

            client.newCall(request).execute().let { response ->
                val body = response.body
                if (body != null) {
                    val parsed = JsonParser.parseString(body.string()).asJsonObject
                    val payload = parsed.get("payload").asJsonArray

                    payload.forEach { o ->
                        val obj = o.asJsonObject
                        val stationName = obj.get("namen").asJsonObject.get("lang").asString
                        val station = Station(
                            stationName,
                            obj.get("land").asString,
                            obj.get("UICCode").asString,
                            obj.get("synoniemen").asJsonArray.map { it.asString }
                        )

                        stations[stationName] = station
                    }
                }
            }
        }

        return stations
    }

    suspend fun getTrips(origin: Station, destination: Station, dateTime: String): ArrayList<Trip> {
        return withContext(Dispatchers.IO) {
            val out = ArrayList<Trip>()
            val url =
                "https://gateway.apiportal.ns.nl/reisinformatie-api/api/v3/trips?" +
                        "originTransit=false&originWalk=false&originBike=false&originCar=false&travelAssistanceTransferTime=0&" +
                        "searchForAccessibleTrip=false&destinationTransit=false&destinationWalk=false&destinationBike=false&" +
                        "destinationCar=false&excludeHighSpeedTrains=false&excludeReservationRequired=false&passing=false&" +
                        "originUicCode=${origin.uicCode}&destinationUicCode=${destination.uicCode}&dateTime=$dateTime"

            val request = requestBase.url(url).build()

            val arrivalKey = "plannedArrivalDateTime"
            val arrivalTrackKey = "plannedArrivalTrack"
            val departureKey = "plannedDepartureDateTime"
            val departureTrackKey = "plannedDepartureTrack"

            client.newCall(request).execute().let { response ->
                val body = response.body
                if (body != null) {
                    val parsed = JsonParser.parseString(body.string()).asJsonObject
                    val payload = parsed.get("trips").asJsonArray

                    payload.forEach { t ->
                        val tripJson = t.asJsonObject
                        val jsonLegs = tripJson.get("legs").asJsonArray
                        val jsonFares = tripJson.get("fares").asJsonArray

                        val legs = mutableListOf<Leg>()
                        jsonLegs.forEach { l ->
                            val legJson = l.asJsonObject
                            val cancelled = legJson.get("cancelled").asBoolean
                            val destinationName =
                                legJson.getAsJsonObject("destination").get("name").asString
                            val stopsJson = legJson.getAsJsonArray("stops")
                            val destinationStopJson = stopsJson
                                .first { it.asJsonObject.get("name").asString == destinationName }.asJsonObject
                            val destinationStation = getAllStations()[destinationName]
                                ?: getAllStations()
                                    .values
                                    .firstOrNull { it.otherNames.contains(destinationName) }

                            if (destinationStation != null) {
                                val destinationStop = Stop(
                                    destinationStation,
                                    arrival = getFieldValue(destinationStopJson, arrivalKey),
                                    arrivalTrack = getFieldValue(destinationStopJson, arrivalTrackKey)
                                )

                                val stops = mutableListOf<Stop>()
                                stopsJson.forEach { s ->
                                    val stopJson = s.asJsonObject
                                    val stopStationName = stopJson.get("name").asString
                                    val stopStation = getAllStations()[stopStationName]

                                    if (stopStation != null) {
                                        val stop = Stop(
                                            stopStation,
                                            arrival = getFieldValue(stopJson, arrivalKey),
                                            arrivalTrack = getFieldValue(stopJson, arrivalTrackKey),
                                            departure = getFieldValue(stopJson, departureKey),
                                            departureTrack = getFieldValue(stopJson, departureTrackKey)
                                        )

                                        stops.add(stop)
                                    } else {
                                        println("stop station $stopStationName seems to be null")
                                    }

                                }

                                legs.add(Leg(cancelled, destinationStop, stops))
                            } else {
                                println("destination $destinationName seems to be null")
                            }
                        }

                        val fares = mutableListOf<Fare>()
                        jsonFares.forEach { f ->
                            val fareJson = f.asJsonObject
                            val productString = fareJson.get("product").asString
                            if (productString.startsWith("OVCHIPKAART")) {
                                val fare = Fare(
                                    fareJson.get("priceInCents").asInt,
                                    productString,
                                    fareJson.get("travelClass").asString,
                                    fareJson.get("discountType").asString
                                )

                                fares.add(fare)
                            }
                        }

                        out.add(Trip(legs, fares))

                    }
                }
            }

            out
        }
    }

    private fun getFieldValue(json: JsonObject, planned: String): String? {
        val actual = planned.replace("planned", "actual")
        if (json.has(actual)) {
            return json.get(actual).asString
        }

        if (json.has(planned)) {
            return json.get(planned).asString
        }

        return null
    }
}