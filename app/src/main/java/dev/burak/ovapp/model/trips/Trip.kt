package dev.burak.ovapp.model.trips

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Trip(
    @SerializedName("legs")
    val legs: List<Leg>,
    @SerializedName("fares")
    val fares: List<Fare>
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
        return getFirstLeg().getDirection()
    }
}
