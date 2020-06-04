package com.burak.android.ovapp.util

import com.ethlo.time.ITU
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

object DateUtil {
    fun createDate(year: Int, month: Int, day: Int, hour: Int, minute: Int): OffsetDateTime {
        val calendar = GregorianCalendar(year, month - 1, day, hour, minute)

        return calendar.toZonedDateTime().toOffsetDateTime()
    }

    fun correctFormatting(raw: String): String {
        return StringBuilder(raw).insert(raw.length - 2, ":").toString()
    }

    fun toTimeString(dateTime: String): String {
        return parseString(dateTime).format(getTimeFormatter())
    }

    fun toDateTimeString(dateTime: String): String {
        return parseString(dateTime).format(getDateTimeFormatter())
    }

    fun toDateTimeString(dateTime: OffsetDateTime): String {
        return dateTime.format(getDateTimeFormatter())
    }

    fun parseString(dateTime: String): OffsetDateTime {
        return ITU.parseDateTime(correctFormatting(dateTime))
    }

    fun getTravelTimeMinutes(a: OffsetDateTime, b: OffsetDateTime): Long {
        return abs(((a.toEpochSecond() - b.toEpochSecond()) / 60))
    }

    fun getDateTimeFormatter(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm", Locale.getDefault())
    }

    fun getTimeFormatter(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    }
}