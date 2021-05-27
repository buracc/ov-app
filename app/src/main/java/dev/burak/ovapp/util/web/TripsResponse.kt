package dev.burak.ovapp.util.web

import com.google.gson.annotations.SerializedName
import dev.burak.ovapp.model.Trip

data class TripsResponse(
    @SerializedName("trips")
    val trips: List<Trip>
)
