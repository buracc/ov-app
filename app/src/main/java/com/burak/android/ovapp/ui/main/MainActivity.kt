package com.burak.android.ovapp.ui.main

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.burak.android.ovapp.R
import com.burak.android.ovapp.util.NSApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    val dateTimeFormatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+02:00'")
        .withZone(ZoneId.of("UTC"))
    val dateTime = LocalDateTime.now(ZoneId.of("UTC"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spType: Spinner = findViewById(R.id.spType)
        ArrayAdapter.createFromResource(
            this,
            R.array.types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spType.adapter = adapter
        }

        initViews()
    }

    private fun initViews() {
        ptDay.setText(dateTime.dayOfMonth.toString())
        ptMonth.setText(dateTime.monthValue.toString())
        ptYear.setText(dateTime.year.toString())
        ptHours.setText(dateTime.hour.toString())
        ptMinutes.setText(dateTime.minute.toString())

        btnSearch.setOnClickListener {
            search()
        }
    }

    private fun search() {
        val origin = ptFrom.text.toString()
        val destination = ptTo.text.toString()
        val dateString = convertDate()

        if (dateString != null) {
            if (origin.isNotBlank()
                && destination.isNotBlank()
            ) {
                GlobalScope.launch {
                    val from = NSApi.getAllStations()[origin] ?: error("hilversum not found")
                    val to = NSApi.getAllStations()[destination] ?: error("sloterdijk not found")

                    println(NSApi.getTrips(from, to)[0])
                }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun convertDate(): String? {
        var formattedDateTime: String? = null
        try {
            val selectedDateTime = LocalDateTime.of(
                ptYear.text.toString().toInt(),
                ptMonth.text.toString().toInt(),
                ptDay.text.toString().toInt(),
                ptHours.text.toString().toInt(),
                ptMinutes.text.toString().toInt()
            )

            formattedDateTime = selectedDateTime.format(dateTimeFormatter)
        } catch (e: DateTimeException) {
            Toast.makeText(this, "Invalid date entered!", Toast.LENGTH_LONG).show()
        }

        return formattedDateTime
    }
}
