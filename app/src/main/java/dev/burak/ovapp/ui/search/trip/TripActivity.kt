package dev.burak.ovapp.ui.search.trip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.burak.ovapp.R
import dev.burak.ovapp.model.trips.adapters.FareAdapter
import dev.burak.ovapp.model.trips.Trip
import dev.burak.ovapp.util.DateUtil
import kotlinx.android.synthetic.main.trip_main.*

@Suppress("DEPRECATION")
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

                val departureTime = DateUtil.parseString(trip.getDepartureTime())
                val arrivalTime = DateUtil.parseString(trip.getArrivalTime())
                val diffMinutes = DateUtil.getTravelTimeMinutes(departureTime, arrivalTime)

                tvDeparture.text = DateUtil.toTimeString(trip.getDepartureTime())
                tvArrival.text = DateUtil.toTimeString(trip.getArrivalTime())
                tvTravelTime.text = "$diffMinutes minutes"
                tvTransfers.text = (trip.legs.size - 1).toString()
                tvTripTitle.text = trip.getDirection().name
            }
        }
    }
}
