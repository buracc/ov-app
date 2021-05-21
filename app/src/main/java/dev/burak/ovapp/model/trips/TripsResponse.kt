package dev.burak.ovapp.model.trips

import com.google.gson.annotations.SerializedName

data class TripsResponse(
    @SerializedName("trips")
    val trips: List<Trip>
)
