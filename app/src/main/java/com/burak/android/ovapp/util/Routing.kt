package com.burak.android.ovapp.util

import android.content.Context
import android.content.Intent
import com.burak.android.ovapp.ui.trips.TripsActivity
import java.time.LocalDateTime

object Routing {
    fun searchTrip(
        origin: String,
        destination: String,
        context: Context,
        dateString: () -> String = {
            val localDateTime = LocalDateTime.now()
            DateUtil.convertDate(
                localDateTime.year,
                localDateTime.monthValue,
                localDateTime.dayOfMonth,
                localDateTime.hour,
                localDateTime.minute
            )
        }
    ): Intent {
        val from = NSApi.getAllStations()[origin] ?: error("$origin not found")
        val to = NSApi.getAllStations()[destination] ?: error("$destination not found")

        val intent = Intent(context, TripsActivity::class.java)
        intent.putParcelableArrayListExtra("trips", NSApi.getTrips(from, to, dateString.invoke()))
        intent.putExtra("from", origin)
        intent.putExtra("to", destination)
        return intent
    }
}