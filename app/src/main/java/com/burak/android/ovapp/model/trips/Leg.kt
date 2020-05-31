package com.burak.android.ovapp.model.trips

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Leg(
    val cancelled: Boolean,
    val destination: Stop,
    val stops: List<Stop>
) : Parcelable {
    fun getOrigin(): Stop {
        return stops.first()
    }

    fun getDepartureTime(): String? {
        return getOrigin().departure
    }

    fun getDeparturePlatform(): String? {
        return getOrigin().departureTrack
    }

    fun getArrivalPlatform(): String? {
        return destination.arrivalTrack
    }

    fun getArrivalTime(): String? {
        return destination.arrival
    }

    fun getDirection(): Stop {
        return stops.last()
    }

    override fun toString(): String {
        return "Leg: \n " +
                "Cancelled: $cancelled \n " +
                "Origin: ${getOrigin()} \n" +
                "Destination: $destination \n " +
                "Stops: ${stops.toString().replace(", ", " -> ")} \n"
    }
}