package dev.burak.ovapp.util

import java.text.NumberFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.math.abs

object FormatUtils {
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    val dateAndTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    val nsDateTimeParser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
    val currencyFormatter = NumberFormat.getCurrencyInstance().also {
        it.maximumFractionDigits = 2
        it.currency = Currency.getInstance("EUR")
    }

    fun getTravelTimeMinutes(a: ZonedDateTime, b: ZonedDateTime): Long {
        return abs(((a.toEpochSecond() - b.toEpochSecond()) / 60))
    }
}
