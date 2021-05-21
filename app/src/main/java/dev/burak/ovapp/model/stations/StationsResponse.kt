package dev.burak.ovapp.model.stations

import com.google.gson.annotations.SerializedName

data class StationsResponse(
    @SerializedName("payload")
    val stations: List<Station>
)
