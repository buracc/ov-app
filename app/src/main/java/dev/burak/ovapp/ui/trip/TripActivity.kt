package dev.burak.ovapp.ui.trip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.burak.ovapp.R
import dev.burak.ovapp.adapter.FareAdapter
import dev.burak.ovapp.model.Trip
import dev.burak.ovapp.adapter.LegAdapter
import dev.burak.ovapp.util.DateUtil
import kotlinx.android.synthetic.main.trip_main.*

class TripActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_main)

        val extras = intent.extras

        if (extras != null) {
            val trip = extras.getParcelable<Trip>("trip")

            if (trip != null) {
                rvFares.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                rvFares.adapter = FareAdapter(trip.fares)

                rvStops.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                rvStops.adapter = LegAdapter(trip.legs)

                val departureTime = DateUtil.parseString(trip.getDepartureTime())
                val arrivalTime = DateUtil.parseString(trip.getArrivalTime())
                val diffMinutes = DateUtil.getTravelTimeMinutes(departureTime, arrivalTime)

                tvStartPlatform.text = trip.getStartPlatform()
                tvDeparture.text = DateUtil.toTimeString(trip.getDepartureTime())
                tvArrival.text = DateUtil.toTimeString(trip.getArrivalTime())
                tvTravelTime.text = "$diffMinutes minutes"
                tvTransferCount.text = (trip.legs.size - 1).toString()
                tvTripTitle.text = trip.getDirection().name ?: "Unknown"
            }
        }
    }
}
