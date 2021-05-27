package dev.burak.ovapp.util.web

import com.google.gson.annotations.SerializedName
import dev.burak.ovapp.model.Station

data class StationsResponse(
    @SerializedName("payload")
    val stations: List<Station>
)
