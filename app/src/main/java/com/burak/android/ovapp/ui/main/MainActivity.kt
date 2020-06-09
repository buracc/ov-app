package com.burak.android.ovapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.burak.android.ovapp.R
import com.burak.android.ovapp.exception.StationNotFoundException
import com.burak.android.ovapp.ui.favourites.FavouritesActivity
import com.burak.android.ovapp.ui.search.SearchActivity
import com.burak.android.ovapp.ui.search.SearchViewModel
import com.burak.android.ovapp.util.DateUtil
import com.burak.android.ovapp.util.NSApi
import com.burak.android.ovapp.util.Routing
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.IllegalStateException
import java.time.DateTimeException
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val spType: Spinner = findViewById(R.id.spType)

        ArrayAdapter.createFromResource(
            this,
            R.array.types,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spType.adapter = it
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
            val dateTime = DateUtil.createDate(
                ptYear.text.toString().toInt(),
                ptMonth.text.toString().toInt(),
                ptDay.text.toString().toInt(),
                ptHours.text.toString().toInt(),
                ptMinutes.text.toString().toInt()
            )

            if (origin.isNotBlank()
                && destination.isNotBlank()
            ) {

                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val from = NSApi.getStationByName(origin)
                        val to = NSApi.getStationByName(destination)
                        val trips = NSApi.getTrips(from, to, dateTime.toZonedDateTime().toString())

                        startActivity(
                            Intent(this@MainActivity, SearchActivity::class.java)
                                .putParcelableArrayListExtra("trips", trips)
                                .putExtra("from", origin)
                                .putExtra("to", destination)
                                .putExtra("dateTime", DateUtil.toDateTimeString(dateTime))
                        )
                    } catch (e: StationNotFoundException) {
                        Toast.makeText(
                            this@MainActivity,
                            "Station ${e.message} not found.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

            } else {
                Toast.makeText(this@MainActivity, "Please fill in all fields.", Toast.LENGTH_LONG)
                    .show()
            }
        } catch (e: DateTimeException) {
            Toast.makeText(this@MainActivity, "Invalid date entered!", Toast.LENGTH_LONG).show()
        }
    }
}
