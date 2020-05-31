package com.burak.android.ovapp.ui.favourites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burak.android.ovapp.R
import com.burak.android.ovapp.model.favourites.Favourite
import com.burak.android.ovapp.model.favourites.adapter.FavouriteAdapter
import com.burak.android.ovapp.util.Routing
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favourites_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavouritesActivity : AppCompatActivity() {

    private lateinit var favouritesViewModel: FavouritesViewModel
    private val favourites = mutableListOf<Favourite>()
    private val favouriteAdapter = FavouriteAdapter(favourites) {
        GlobalScope.launch {
            startActivity(Routing.searchTrip(it.from, it.to, this@FavouritesActivity))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favourites_main)

        initViewModel()
        rvFavourites.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvFavourites.adapter = favouriteAdapter
        createSwipeHelper().attachToRecyclerView(rvFavourites)
    }

    private fun initViewModel() {
        favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModel::class.java)
        favouritesViewModel.favourites.observe(this, Observer { g ->
            this.favourites.clear()

            val sortedList = g.sortedWith(compareBy { it.from })
            this.favourites.addAll(sortedList)

            favouriteAdapter.notifyDataSetChanged()
        })
    }

    private fun createSwipeHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val delete = favourites[position]
                favouritesViewModel.deleteFavourite(delete)
                Snackbar.make(rvFavourites, "Succesfully deleted: $delete", Snackbar.LENGTH_SHORT).show()
            }
        }

        return ItemTouchHelper(callback)
    }
}