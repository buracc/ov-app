package dev.burak.ovapp.ui.trip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.burak.ovapp.R
import dev.burak.ovapp.model.Trip
import dev.burak.ovapp.adapter.LegAdapter
import dev.burak.ovapp.util.FormatUtils
import kotlinx.android.synthetic.main.trip_main.*
import java.time.ZonedDateTime

@AndroidEntryPoint
class TripActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_main)

        val extras = intent.extras

        if (extras != null) {
            val trip = extras.getParcelable<Trip>("trip")

            if (trip != null) {
//                rvFares.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
//                rvFares.adapter = FareAdapter(trip.fares)

                rvStops.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                rvStops.adapter = LegAdapter(trip.legs)

                val departureDateTime = ZonedDateTime.parse(trip.getDepartureTime(), FormatUtils.nsDateTimeParser)
                val arrivalDateTime = ZonedDateTime.parse(trip.getArrivalTime(), FormatUtils.nsDateTimeParser)
                val diffMinutes = FormatUtils.getTravelTimeMinutes(departureDateTime, arrivalDateTime)

                tvStartPlatform.text = trip.getStartPlatform()
                tvDeparture.text = departureDateTime.toLocalTime().toString()
                tvArrival.text = arrivalDateTime.toLocalTime().toString()
                tvTravelTime.text = "$diffMinutes minutes"
                tvTransferCount.text = (trip.legs.size - 1).toString()
                tvTripTitle.text = trip.getDirection().name ?: "Unknown"

                tvFirstClassDiscount.text = "${FormatUtils.currencyFormatter.format(trip.fares.first { 
                    it.product == "OVCHIPKAART_ENKELE_REIS"
                            && it.discountType == "DISCOUNT_40_PERCENT"
                            && it.travelClass == "FIRST_CLASS"
                }.priceCents.toDouble() / 100)}"
                tvFirstClassFull.text = "${FormatUtils.currencyFormatter.format(trip.fares.first {
                    it.product == "OVCHIPKAART_ENKELE_REIS"
                            && it.discountType == "NO_DISCOUNT"
                            && it.travelClass == "FIRST_CLASS"
                }.priceCents.toDouble() / 100)}"
                tvSecondClassDiscount.text = "${FormatUtils.currencyFormatter.format(trip.fares.first {
                    it.product == "OVCHIPKAART_ENKELE_REIS"
                            && it.discountType == "DISCOUNT_40_PERCENT"
                            && it.travelClass == "SECOND_CLASS"
                }.priceCents.toDouble() / 100)}"
                tvSecondClassFull.text = "${FormatUtils.currencyFormatter.format(trip.fares.first {
                    it.product == "OVCHIPKAART_ENKELE_REIS"
                            && it.discountType == "NO_DISCOUNT"
                            && it.travelClass == "SECOND_CLASS"
                }.priceCents.toDouble() / 100)}"
            }
        }
    }
}
