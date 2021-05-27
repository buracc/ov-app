package dev.burak.ovapp.util.web

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OvApi {
    @GET("/reisinformatie-api/api/v2/stations")
    suspend fun getStations(): Response<StationsResponse>

    @GET("/reisinformatie-api/api/v3/trips")
    suspend fun getTrips(
        @Query("originUicCode") uicCode: String,
        @Query("destinationUicCode") destinationUicCode: String,
        @Query("dateTime") dateTime: String,
    ): Response<TripsResponse>
}
