package com.burak.android.ovapp.model.favourites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.burak.android.ovapp.R
import kotlinx.android.synthetic.main.favourite_card.view.*

class FavouriteAdapter(private val favourites: List<Favourite>) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    lateinit var context: Context

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.favourite_card, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return favourites.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favourites[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(favourite: Favourite) {
            itemView.tvFrom.text = favourite.from
            itemView.tvTo.text = favourite.to
        }
    }
}
