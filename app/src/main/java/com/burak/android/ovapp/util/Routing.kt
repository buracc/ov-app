package com.burak.android.ovapp.util

import android.content.Context
import android.content.Intent
import com.burak.android.ovapp.exception.StationNotFoundException
import com.burak.android.ovapp.ui.search.SearchActivity
import java.lang.Exception
import java.time.OffsetDateTime

object Routing {
//    fun searchTrip(
//        origin: String,
//        destination: String,
//        context: Context,
//        dateTime: OffsetDateTime = OffsetDateTime.now()
//    ): Intent {
//        val from = NSApi.getAllStations()[origin]
//        val to = NSApi.getAllStations()[destination]
//        val formattedDate = dateTime.toZonedDateTime().toString()
//
//        if (from == null) {
//            throw StationNotFoundException(origin)
//        }
//
//        if (to == null) {
//            throw StationNotFoundException(destination)
//        }
//
//        return Intent(context, SearchActivity::class.java)
//            .putParcelableArrayListExtra("trips", NSApi.getTrips(from, to, formattedDate))
//            .putExtra("from", origin)
//            .putExtra("to", destination)
//            .putExtra("dateTime", DateUtil.toDateTimeString(dateTime))
//    }
}