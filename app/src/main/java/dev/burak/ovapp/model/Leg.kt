package dev.burak.ovapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Leg(
    val cancelled: Boolean,
    val stops: List<Stop>,
    val origin: Stop,
    val destination: Stop
) : Parcelable {
    fun getDepartureTime(): String {
        return origin.dateTime
    }

    fun getDeparturePlatform(): String {
        return origin.track
    }

    fun getArrivalPlatform(): String {
        return destination.track
    }

    fun getArrivalTime(): String {
        return destination.dateTime
    }

    override fun toString(): String {
        return "Leg: \n " +
                "Cancelled: $cancelled \n " +
                "Origin: $origin \n" +
                "Destination: $destination \n " +
                "Stops: ${stops.map { it.name }.toString().replace(", ", " -> ")} \n"
    }
}
