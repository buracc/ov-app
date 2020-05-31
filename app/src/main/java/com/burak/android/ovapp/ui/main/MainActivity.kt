package com.burak.android.ovapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.burak.android.ovapp.R
import com.burak.android.ovapp.ui.favourites.FavouritesActivity
import com.burak.android.ovapp.ui.trips.TripsActivity
import com.burak.android.ovapp.util.DateUtil
import com.burak.android.ovapp.util.NSApi
import com.burak.android.ovapp.util.Routing
import com.ethlo.time.ITU
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class MainActivity : AppCompatActivity() {

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
        val now = LocalDateTime.now()
        ptDay.setText(now.dayOfMonth.toString())
        ptMonth.setText(now.monthValue.toString())
        ptYear.setText(now.year.toString())
        ptHours.setText(now.hour.toString())
        ptMinutes.setText(now.minute.toString())

        btnSearch.setOnClickListener {
            search()
        }

        btnFavourites.setOnClickListener {
            val intent = Intent(this@MainActivity, FavouritesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun search() {
        try {
            val origin = ptFrom.text.toString()
            val destination = ptTo.text.toString()
            val dateString = DateUtil.convertDate(
                ptYear.text.toString().toInt(),
                ptMonth.text.toString().toInt(),
                ptDay.text.toString().toInt(),
                ptHours.text.toString().toInt(),
                ptMinutes.text.toString().toInt()
            )

            if (origin.isNotBlank()
                && destination.isNotBlank()
            ) {

                GlobalScope.launch {
                    startActivity(
                        Routing.searchTrip(
                            origin,
                            destination,
                            this@MainActivity
                        ) { dateString }
                    )
                }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_LONG).show()
            }
        } catch (e: DateTimeException) {
            Toast.makeText(this, "Invalid date entered!", Toast.LENGTH_LONG).show()
        }
    }
}
