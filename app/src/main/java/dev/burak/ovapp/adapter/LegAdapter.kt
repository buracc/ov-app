package dev.burak.ovapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.burak.ovapp.R
import dev.burak.ovapp.model.Leg
import dev.burak.ovapp.util.FormatUtils
import kotlinx.android.synthetic.main.stop_card.view.*
import java.lang.Exception
import java.time.LocalDateTime

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
            val departure = try {
                LocalDateTime.parse(leg.origin.dateTime, FormatUtils.nsDateTimeParser).toLocalTime().toString()
            } catch (e: Exception) {
                Log.e("DateTime Error", e.message, e)
                "Unknown"
            }

            itemView.tvDepInfo.text = "Track ${leg.origin.track}, $departure"

            val arrival = try {
                LocalDateTime.parse(leg.destination.dateTime, FormatUtils.nsDateTimeParser).toLocalTime().toString()
            } catch (e: Exception) {
                Log.e("DateTime Error", e.message, e)
                "Unknown"
            }

            itemView.tvArrStation.text = leg.destination.name
            itemView.tvArrInfo.text = "Track ${leg.destination.track}, $arrival"
        }
    }
}
