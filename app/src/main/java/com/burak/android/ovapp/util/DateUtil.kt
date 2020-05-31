package com.burak.android.ovapp.util

import com.ethlo.time.ITU
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

object DateUtil {

    fun convertDate(year: Int, month: Int, day: Int, hour: Int, minute: Int): String {
        val selectedDateTime = LocalDateTime.of(
            year,
            month,
            day,
            hour,
            minute
        )
        val selectedDate = Date.from(selectedDateTime.toInstant(ZoneOffset.ofHours(2)))
        return ITU.format(selectedDate, ZoneOffset.UTC.id)
    }
}