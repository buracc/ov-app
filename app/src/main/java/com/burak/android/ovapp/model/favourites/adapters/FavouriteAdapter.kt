package com.burak.android.ovapp.model.favourites.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.burak.android.ovapp.R
import com.burak.android.ovapp.model.favourites.Favourite
import kotlinx.android.synthetic.main.favourite_card.view.*

class FavouriteAdapter(
    private val favourites: List<Favourite>,
    private val listener: (Favourite) -> Unit
) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.favourite_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favourites[position])
        holder.itemView.setOnClickListener {
            favourites[position].let {
                listener.invoke(it)
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favourite: Favourite) {
            itemView.tvFrom.text = favourite.from
            itemView.tvTo.text = favourite.to
        }
    }
}
