package dev.burak.ovapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.burak.ovapp.R
import dev.burak.ovapp.exception.UncaughtExceptionHandler
import dev.burak.ovapp.model.Station
import dev.burak.ovapp.model.Trip
import dev.burak.ovapp.ui.favourites.FavouritesActivity
import dev.burak.ovapp.ui.main.pickers.DatePickerFragment
import dev.burak.ovapp.ui.main.pickers.TimePickerFragment
import dev.burak.ovapp.ui.search.SearchActivity
import dev.burak.ovapp.util.DateUtil
import dev.burak.ovapp.util.web.OvApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.net.UnknownHostException
import java.time.DateTimeException
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var ovApi: OvApi

    fun getStations() =
        try {
            runBlocking(Dispatchers.IO) {
                return@runBlocking ovApi.getStations().body()?.stations ?: return@runBlocking emptyList()
            }
        } catch (e: UnknownHostException) {
            Log.d("Autocomplete", e.message, e)
            Toast.makeText(
                this@MainActivity,
                "Couldn't retrieve stations, are you connected to the internet?",
                Toast.LENGTH_LONG
            ).show()
            emptyList()
        }

    private lateinit var stationsAdapter: ArrayAdapter<Station>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stationsAdapter = ArrayAdapter(
            this@MainActivity,
            R.layout.dropdown_item,
            getStations().filter { it.country == "NL" }
        )

        initViews()

        Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler(this))
    }

    override fun onResume() {
        super.onResume()
        searchProgressBar.visibility = View.INVISIBLE
        stationsAdapter.clear()
        stationsAdapter.addAll(getStations().filter { it.country == "NL" })
    }

    private fun initViews() {
        val now = LocalDateTime.now()
        ptDate.text = "${now.dayOfMonth}-${now.monthValue}-${now.year}"
        val min = if (now.minute < 10) {
            "0${now.minute}"
        } else {
            now.minute.toString()
        }
        ptTime.text = "${now.hour}:${min}"

        btnSearch.setOnClickListener {
            searchProgressBar.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.Main) {
                val intent =
                    try {
                        search()
                    } catch (e: UnknownHostException) {
                        Log.d("Search trips", e.message, e)
                        Toast.makeText(
                            this@MainActivity,
                            "Couldn't plan trip, are you connected to the internet?",
                            Toast.LENGTH_LONG
                        ).show()
                        null
                    }

                if (intent != null) {
                    startActivity(intent)
                }

                searchProgressBar.visibility = View.INVISIBLE
            }
        }

        btnFavourites.setOnClickListener {
            val intent = Intent(this@MainActivity, FavouritesActivity::class.java)
            startActivity(intent)
        }

        val originAutoComplete = findViewById<AutoCompleteTextView>(R.id.ptFrom)
        originAutoComplete.setAdapter(stationsAdapter)
        originAutoComplete.threshold = 1

        val destinationAutoComplete = findViewById<AutoCompleteTextView>(R.id.ptTo)
        destinationAutoComplete.setAdapter(stationsAdapter)
        destinationAutoComplete.threshold = 1

        val stations = getStations().filter { it.country == "NL" }

        ptFrom.setText(stations.randomOrNull()?.name)
        ptTo.setText(stations.randomOrNull()?.name)
    }

    private suspend fun search(): Intent? {
        val stations = getStations()
        if (stations.isEmpty()) {
            return null
        }

        val origin = stations.firstOrNull { it.names.values.contains(ptFrom.text.toString()) }
        val destination = stations.firstOrNull { it.names.values.contains(ptTo.text.toString()) }
        val date = ptDate.text.toString().split("-")
        val time = ptTime.text.toString().split(":")
        val dateTime = DateUtil.createDate(
            date[2].toInt(),
            date[1].toInt(),
            date[0].toInt(),
            time[0].toInt(),
            time[1].toInt()
        )

        if (origin == null) {
            Toast.makeText(
                this@MainActivity,
                "Origin station ${ptFrom.text} not found.",
                Toast.LENGTH_LONG
            ).show()
            return null
        }

        if (destination == null) {
            Toast.makeText(
                this@MainActivity,
                "Destination station ${ptTo.text} not found.",
                Toast.LENGTH_LONG
            ).show()
            return null
        }

        val result = GlobalScope.async(Dispatchers.IO) {
            val response = ovApi.getTrips(
                origin.uicCode, destination.uicCode, dateTime.toZonedDateTime().toString()
            )

            Log.d("Request", "${origin.uicCode}, ${destination.uicCode}, ${dateTime.toZonedDateTime().toString()}")
            Log.d("Response", response.toString())

            val responseBody = response.body() ?: return@async null

            Log.d("Response", responseBody.toString())

            val trips = responseBody.trips
            return@async Intent(this@MainActivity, SearchActivity::class.java)
                .putParcelableArrayListExtra("trips", arrayListOf<Trip>().also {
                    trips.forEach { t -> it.add(t) }
                })
                .putExtra("from", origin)
                .putExtra("to", destination)
                .putExtra("dateTime", DateUtil.toDateTimeString(dateTime))
        }.await()

        if (result == null) {
            Toast.makeText(
                this@MainActivity,
                "Can't plan a trip for these stations :(.",
                Toast.LENGTH_LONG
            ).show()
        }

        return result
    }

    private fun showTimePickerDialog(v: View) {
        TimePickerFragment(this).show(supportFragmentManager, "timePicker")
    }

    private fun showDatePickerDialog(v: View) {
        DatePickerFragment(this).show(supportFragmentManager, "datePicker")
    }
}
