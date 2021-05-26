package dev.burak.ovapp.model.trips.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.burak.ovapp.R
import dev.burak.ovapp.model.trips.Fare
import kotlinx.android.synthetic.main.fare_card.view.*
import kotlin.text.Typography.euro

class FareAdapter(val fares: List<Fare>) : RecyclerView.Adapter<FareAdapter.ViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.fare_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return fares.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(fares[position])
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(fare: Fare) {
            itemView.tvProduct.text = fare.product
            itemView.tvPrice.text = "$euro${(fare.priceCents.toDouble() / 100)}"
            itemView.tvClass.text = fare.travelClass
            itemView.tvDiscount.text = fare.discountType
        }
    }
}
