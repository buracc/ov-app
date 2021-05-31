package dev.burak.ovapp.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.burak.ovapp.R
import dev.burak.ovapp.model.Trip
import dev.burak.ovapp.util.FormatUtils
import kotlinx.android.synthetic.main.search_card.view.*
import kotlinx.android.synthetic.main.search_card.view.tvArrival
import kotlinx.android.synthetic.main.search_card.view.tvDeparture
import java.lang.Exception
import java.time.LocalDateTime

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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(trip: Trip) {
            val departureTime = try {
                LocalDateTime.parse(trip.getDepartureTime(), FormatUtils.nsDateTimeParser).toLocalTime().toString()
            } catch (e: Exception) {
                Log.e("DateTime Error", e.message, e)
                "Unknown"
            }

            val arrivalTime = try {
                LocalDateTime.parse(trip.getArrivalTime(), FormatUtils.nsDateTimeParser).toLocalTime().toString()
            } catch (e: Exception) {
                Log.e("DateTime Error", e.message, e)
                "Unknown"
            }


            itemView.tvPlatform.text = trip.getStartPlatform()

            if (trip.status == "CANCELLED") {
                itemView.tripCard.setCardBackgroundColor(Color.rgb(245, 198, 184))
                itemView.tvStatus.text = "Cancelled"
                itemView.tvPlatform.text = "Cancelled"
            }

            itemView.tvDirection.text = trip.getDirection().name ?: "Unknown"
            itemView.tvDeparture.text = departureTime
            itemView.tvArrival.text = arrivalTime
        }
    }
}
