package dev.burak.ovapp.model.trips

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Leg(
    @SerializedName("cancelled")
    val cancelled: Boolean,
    @SerializedName("stops")
    val stops: List<Stop>
) : Parcelable {
    fun getOrigin(): Stop {
        return stops.first()
    }

    fun getDepartureTime(): String {
        return getOrigin().departureDateTime
    }

    fun getDeparturePlatform(): String {
        return getOrigin().departureTrack
    }

    fun getArrivalPlatform(): String {
        return getDirection().arrivalTrack
    }

    fun getArrivalTime(): String {
        return getDirection().arrivalDateTime
    }

    fun getDirection(): Stop {
        return stops.last()
    }

    override fun toString(): String {
        return "Leg: \n " +
                "Cancelled: $cancelled \n " +
                "Origin: ${getOrigin()} \n" +
                "Destination: ${getDirection()} \n " +
                "Stops: ${stops.toString().replace(", ", " -> ")} \n"
    }
}
