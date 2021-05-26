package dev.burak.ovapp.model.trips.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.burak.ovapp.R
import dev.burak.ovapp.model.trips.Leg
import dev.burak.ovapp.model.trips.Stop
import dev.burak.ovapp.util.DateUtil
import kotlinx.android.synthetic.main.stop_card.view.*

class LegAdapter(val legs: List<Leg>) : RecyclerView.Adapter<LegAdapter.ViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.stop_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return legs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(legs[position])
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(leg: Leg) {
            itemView.tvDepStation.text = leg.origin.name
            val departure = if (leg.origin.dateTime == "Unknown") {
                "Unknown"
            } else {
                DateUtil.toTimeString(leg.origin.dateTime)
            }

            itemView.tvDepInfo.text = "Track ${leg.origin.track}, $departure"

            val arrival = if (leg.destination.dateTime == "Unknown") {
                "Unknown"
            } else {
                DateUtil.toTimeString(leg.destination.dateTime)
            }

            itemView.tvArrStation.text = leg.destination.name
            itemView.tvArrInfo.text = "Track ${leg.destination.track}, $arrival"
        }
    }
}
