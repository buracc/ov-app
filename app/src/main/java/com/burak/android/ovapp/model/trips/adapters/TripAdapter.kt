package com.burak.android.ovapp.model.trips.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.burak.android.ovapp.R
import com.burak.android.ovapp.model.trips.Trip
import com.burak.android.ovapp.util.DateUtil
import kotlinx.android.synthetic.main.search_card.view.*

class TripAdapter(
    val trips: List<Trip>,
    val listener: (Trip) -> Unit
) : RecyclerView.Adapter<TripAdapter.ViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.search_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trips[position])
        holder.itemView.setOnClickListener {
            trips[position].let {
                listener.invoke(it)
            }
        }
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(trip: Trip) {
            var departureTime = trip.getDepartureTime()
            var arrivalTime = trip.getArrivalTime()

            if (departureTime != null) {
                departureTime = DateUtil.toTimeString(departureTime)
            }

            if (arrivalTime != null) {
                arrivalTime = DateUtil.toTimeString(arrivalTime)
            }

            itemView.tvDirection.text = trip.getDirection().station.name
            itemView.tvDeparture.text = departureTime
            itemView.tvArrival.text = arrivalTime
            itemView.tvPlatform.text = trip.getStartPlatform()
        }
    }
}