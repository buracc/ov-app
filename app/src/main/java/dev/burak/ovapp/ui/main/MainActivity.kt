package dev.burak.ovapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.burak.ovapp.R
import dev.burak.ovapp.exception.StationNotFoundException
import dev.burak.ovapp.model.trips.Trip
import dev.burak.ovapp.ui.favourites.FavouritesActivity
import dev.burak.ovapp.ui.search.SearchActivity
import dev.burak.ovapp.util.DateUtil
import dev.burak.ovapp.util.OvApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.time.DateTimeException
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var ovApi: OvApi

    fun getStations() = runBlocking(Dispatchers.IO) {
        return@runBlocking ovApi.getStations().body()?.stations ?: return@runBlocking emptyList()
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
            getStations()
        )

        val originAutoComplete = findViewById<AutoCompleteTextView>(R.id.ptFrom)
        originAutoComplete.setAdapter(stationsAdapter)
        originAutoComplete.threshold = 1

        val destinationAutoComplete = findViewById<AutoCompleteTextView>(R.id.ptTo)
        destinationAutoComplete.setAdapter(stationsAdapter)
        destinationAutoComplete.threshold = 1

        val stations = getStations()
        ptFrom.setText(stations.random().name)
        ptTo.setText(stations.random().name)
    }

    private fun search() {
        try {
            val stations = getStations()
            val origin = stations.firstOrNull { it.names.values.contains(ptFrom.text.toString()) }
            val destination = stations.firstOrNull { it.names.values.contains(ptTo.text.toString()) }
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
                        val request = ovApi.getTrips(
                            origin.uicCode, destination.uicCode, dateTime.toZonedDateTime().toString()
                        )
                        println(request)
                        val trips = request.body()?.trips ?: return@launch
                        val arraylist = arrayListOf<Trip>().also {
                            trips.forEach { t -> it.add(t) }
                        }
                        startActivity(
                            Intent(this@MainActivity, SearchActivity::class.java)
                                .putParcelableArrayListExtra("trips", arraylist)
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
