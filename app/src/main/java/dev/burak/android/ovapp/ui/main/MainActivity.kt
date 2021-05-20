package dev.burak.android.ovapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.burak.android.ovapp.R
import dev.burak.android.ovapp.exception.StationNotFoundException
import dev.burak.android.ovapp.model.stations.Station
import dev.burak.android.ovapp.ui.favourites.FavouritesActivity
import dev.burak.android.ovapp.ui.search.SearchActivity
import dev.burak.android.ovapp.util.DateUtil
import dev.burak.android.ovapp.util.NSApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.time.DateTimeException
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    val stations = runBlocking(Dispatchers.IO) {
        return@runBlocking NSApi.getAllStations()
    }

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

        val stationsAdapter = ArrayAdapter(
            this@MainActivity,
            android.R.layout.select_dialog_item,
            stations.values.toTypedArray()
        )

        val originAutoComplete = findViewById<AutoCompleteTextView>(R.id.ptFrom)
        originAutoComplete.setAdapter(stationsAdapter)
        originAutoComplete.threshold = 1

        val destinationAutoComplete = findViewById<AutoCompleteTextView>(R.id.ptTo)
        destinationAutoComplete.setAdapter(stationsAdapter)
        destinationAutoComplete.threshold = 1
    }

    private fun search() {
        try {
            val origin = stations[ptFrom.text.toString()]
            val destination = stations[ptTo.text.toString()]
            val dateTime = DateUtil.createDate(
                ptYear.text.toString().toInt(),
                ptMonth.text.toString().toInt(),
                ptDay.text.toString().toInt(),
                ptHours.text.toString().toInt(),
                ptMinutes.text.toString().toInt()
            )

            if (origin != null && destination != null) {
                try {
                    GlobalScope.launch(Dispatchers.IO) {
                        val trips = NSApi.getTrips(origin, destination, dateTime.toZonedDateTime().toString())
                        startActivity(
                            Intent(this@MainActivity, SearchActivity::class.java)
                                .putParcelableArrayListExtra("trips", trips)
                                .putExtra("from", origin)
                                .putExtra("to", destination)
                                .putExtra("dateTime", DateUtil.toDateTimeString(dateTime))
                        )
                    }
                } catch (e: StationNotFoundException) {
                    Toast.makeText(
                        this@MainActivity,
                        "Station ${e.message} not found.",
                        Toast.LENGTH_LONG
                    ).show()
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
