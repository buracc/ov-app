package dev.burak.ovapp.util

import dev.burak.ovapp.model.stations.StationsResponse
import dev.burak.ovapp.model.trips.TripsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OvApi {
    @GET("/reisinformatie-api/api/v2/stations")
    suspend fun getStations(): Response<StationsResponse>

    @GET("https://gateway.apiportal.ns.nl/reisinformatie-api/api/v3/trips?" +
            "originTransit=false&originWalk=false&originBike=false&originCar=false&travelAssistanceTransferTime=0&" +
            "searchForAccessibleTrip=false&destinationTransit=false&destinationWalk=false&destinationBike=false&" +
            "destinationCar=false&excludeHighSpeedTrains=false&excludeReservationRequired=false&passing=false")
    suspend fun getTrips(
        @Query("originUicCode") uicCode: String,
        @Query("destinationUicCode") destinationUicCode: String,
        @Query("dateTime") dateTime: String,
    ): Response<TripsResponse>
}
