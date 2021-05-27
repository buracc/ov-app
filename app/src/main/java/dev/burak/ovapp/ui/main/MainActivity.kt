package dev.burak.ovapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.burak.ovapp.R
import dev.burak.ovapp.model.Trip
import dev.burak.ovapp.ui.favourites.FavouritesActivity
import dev.burak.ovapp.ui.main.pickers.DatePickerFragment
import dev.burak.ovapp.ui.main.pickers.TimePickerFragment
import dev.burak.ovapp.ui.search.SearchActivity
import dev.burak.ovapp.util.DateUtil
import dev.burak.ovapp.util.web.OvApi
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
        initViews()
    }

    override fun onResume() {
        super.onResume()
        searchProgressBar.visibility = View.INVISIBLE
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
                val intent = search()
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

        val stations = getStations().filter { it.country == "NL" }
        val stationsAdapter = ArrayAdapter(
            this@MainActivity,
            R.layout.dropdown_item,
            stations
        )

        val originAutoComplete = findViewById<AutoCompleteTextView>(R.id.ptFrom)
        originAutoComplete.setAdapter(stationsAdapter)
        originAutoComplete.threshold = 1

        val destinationAutoComplete = findViewById<AutoCompleteTextView>(R.id.ptTo)
        destinationAutoComplete.setAdapter(stationsAdapter)
        destinationAutoComplete.threshold = 1

        ptFrom.setText(stations.random().name)
        ptTo.setText(stations.random().name)
    }

    private suspend fun search(): Intent? {
        val stations = getStations()
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

    fun showTimePickerDialog(v: View) {
        TimePickerFragment(this).show(supportFragmentManager, "timePicker")
    }

    fun showDatePickerDialog(v: View) {
        DatePickerFragment(this).show(supportFragmentManager, "datePicker")
    }
}
