package dev.burak.ovapp.model.trips

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Stop(
    val uicCode: String?,
    val name: String?,
    val countryCode: String?,
    private val plannedArrivalTrack: String?,
    private val actualArrivalTrack: String?,
    private val plannedArrivalDateTime: String?,
    private val actualArrivalDateTime: String?,
    private val actualDepartureTrack: String?,
    private val plannedDepartureTrack: String?,
    private val plannedDepartureDateTime: String?,
    private val actualDepartureDateTime: String?
) : Parcelable {
    val arrivalTrack: String
        get() = actualArrivalTrack ?: plannedArrivalTrack ?: "Unknown"
    val arrivalDateTime: String
        get() = actualArrivalDateTime ?: plannedArrivalDateTime ?: "Unknown"
    val departureTrack: String
        get() = actualDepartureTrack ?: plannedDepartureTrack ?: "Unknown"
    val departureDateTime: String
        get() = actualDepartureDateTime ?: plannedDepartureDateTime ?: "Unknown"
}
