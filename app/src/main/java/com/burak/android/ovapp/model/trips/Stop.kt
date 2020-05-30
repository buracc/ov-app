package com.burak.android.ovapp.model.trips

import com.burak.android.ovapp.model.stations.Station
import java.lang.StringBuilder

data class Stop(
    val station: Station,
    val arrival: String? = null,
    val arrivalTrack: String? = null,
    val departure: String? = null,
    val departureTrack: String? = null
) {
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