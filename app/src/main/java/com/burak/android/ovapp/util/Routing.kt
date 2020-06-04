package com.burak.android.ovapp.util

import android.content.Context
import android.content.Intent
import com.burak.android.ovapp.ui.search.SearchActivity
import java.time.OffsetDateTime

object Routing {
    fun searchTrip(
        origin: String,
        destination: String,
        context: Context,
        dateTime: OffsetDateTime = OffsetDateTime.now()
    ): Intent {
        val from = NSApi.getAllStations()[origin] ?: error("$origin not found")
        val to = NSApi.getAllStations()[destination] ?: error("$destination not found")
        val formattedDate = dateTime.toZonedDateTime().toString()

        val intent = Intent(context, SearchActivity::class.java)
        intent.putParcelableArrayListExtra("trips", NSApi.getTrips(from, to, formattedDate))
        intent.putExtra("from", origin)
        intent.putExtra("to", destination)
        intent.putExtra("dateTime", DateUtil.toDateTimeString(dateTime))

        return intent
    }
}