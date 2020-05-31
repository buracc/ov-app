package com.burak.android.ovapp.model.trips

import android.os.Parcelable
import com.burak.android.ovapp.model.stations.Station
import kotlinx.android.parcel.Parcelize
import java.lang.StringBuilder

@Parcelize
data class Stop(
    val station: Station,
    val arrival: String? = null,
    val arrivalTrack: String? = null,
    val departure: String? = null,
    val departureTrack: String? = null
) : Parcelable {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(" { ")
        sb.append(station)

        if (arrival != null) {
            sb.append(" | Arrival: $arrival")
        }

        if (arrivalTrack != null) {
            sb.append(" | Arrival Platform: $arrivalTrack")
        }

        if (departure != null) {
            sb.append(" | Departure: $departure")
        }

        if (departureTrack != null) {
            sb.append(" | Departure Platform: $departureTrack")
        }

        sb.append(" } ")

        return sb.toString()
    }
}