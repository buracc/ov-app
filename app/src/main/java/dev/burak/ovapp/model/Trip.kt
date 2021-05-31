package dev.burak.ovapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Trip(
    val legs: List<Leg>,
    val fares: List<Fare>,
    val status: String
): Parcelable {
    fun getFirstLeg(): Leg {
        return legs.first()
    }

    fun getLastLeg(): Leg {
        return legs.last()
    }

    fun getDepartureTime(): String {
        return getFirstLeg().getDepartureTime()
    }

    fun getArrivalTime(): String {
        return getLastLeg().getArrivalTime()
    }

    fun getStartPlatform(): String {
        return getFirstLeg().getDeparturePlatform()
    }

    fun getDirection(): Stop {
        return getFirstLeg().destination
    }
}
