package com.burak.android.ovapp.ui.search.trip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burak.android.ovapp.R
import com.burak.android.ovapp.model.trips.Trip
import com.burak.android.ovapp.model.trips.adapters.FareAdapter
import com.burak.android.ovapp.util.DateUtil
import kotlinx.android.synthetic.main.trip_main.*

@Suppress("DEPRECATION")
class TripActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_main)

        if (intent.extras != null) {
            if (intent.hasExtra("trip")) {
                val trip = intent.getParcelableExtra<Trip>("trip")

                if (trip != null) {
                    rvFares.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                    rvFares.adapter = FareAdapter(trip.fares)

                    val departureTime = DateUtil.parseString(trip.getDepartureTime()!!)
                    val arrivalTime = DateUtil.parseString(trip.getArrivalTime()!!)
                    val diffMinutes = DateUtil.getTravelTimeMinutes(departureTime, arrivalTime)

                    tvDeparture.text = DateUtil.toTimeString(trip.getDepartureTime()!!)
                    tvArrival.text = DateUtil.toTimeString(trip.getArrivalTime()!!)
                    tvTravelTime.text = "$diffMinutes minutes"
                    tvTransfers.text = (trip.legs.size - 1).toString()
                    tvTripTitle.text = trip.getDirection().station.name
                }
            }
        }
    }
}