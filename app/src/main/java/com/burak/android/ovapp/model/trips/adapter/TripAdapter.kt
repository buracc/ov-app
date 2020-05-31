package com.burak.android.ovapp.model.trips.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.burak.android.ovapp.R
import com.burak.android.ovapp.model.trips.Trip
import com.ethlo.time.ITU
import kotlinx.android.synthetic.main.trip_card.view.*
import java.lang.StringBuilder
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class TripAdapter(val trips: List<Trip>) : RecyclerView.Adapter<TripAdapter.ViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.trip_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trips[position])
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(trip: Trip) {
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)
            var departureTime = trip.getDepartureTime()
            var arrivalTime = trip.getArrivalTime()

            if (departureTime != null) {
                val correctFormat = StringBuilder(departureTime).insert(departureTime.length - 2, ":")
                departureTime = ITU.parseDateTime(correctFormat.toString()).format(formatter)
            }

            if (arrivalTime != null) {
                val correctFormat = StringBuilder(arrivalTime).insert(arrivalTime.length - 2, ":")
                arrivalTime = ITU.parseDateTime(correctFormat.toString()).format(formatter)
            }

            itemView.tvDirection.text = trip.getDirection().station.name
            itemView.tvDeparture.text = departureTime
            itemView.tvArrival.text = arrivalTime
            itemView.tvPlatform.text = trip.getStartPlatform()
        }
    }
}