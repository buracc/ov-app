package dev.burak.android.ovapp.ui.favourites

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.burak.android.ovapp.R
import dev.burak.android.ovapp.exception.StationNotFoundException
import dev.burak.android.ovapp.model.favourites.Favourite
import dev.burak.android.ovapp.model.favourites.adapters.FavouriteAdapter
import dev.burak.android.ovapp.ui.search.SearchActivity
import dev.burak.android.ovapp.util.DateUtil
import dev.burak.android.ovapp.util.NSApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favourites_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

@Suppress("DEPRECATION")
class FavouritesActivity : AppCompatActivity() {

    private lateinit var favouritesViewModel: FavouritesViewModel
    private val favourites = mutableListOf<Favourite>()
    private val favouriteAdapter = FavouriteAdapter(favourites) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val from = NSApi.getStationByName(it.from)
                val to = NSApi.getStationByName(it.to)
                val dateTime = OffsetDateTime.now().toZonedDateTime().toString()
                val trips = NSApi.getTrips(from, to, dateTime)

                startActivity(
                    Intent(this@FavouritesActivity, SearchActivity::class.java)
                        .putParcelableArrayListExtra("trips", trips)
                        .putExtra("from", from)
                        .putExtra("to", to)
                        .putExtra("dateTime", DateUtil.toDateTimeString(dateTime, false))
                )
            } catch (e: StationNotFoundException) {
                Toast.makeText(
                    this@FavouritesActivity,
                    "Station ${e.message} not found.",
                    Toast.LENGTH_LONG
                ).show()
            }
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
        favouritesViewModel.favourites.observe(this) { g ->
            favourites.clear()

            val sortedList = g.sortedWith(compareBy { it.from })
            favourites.addAll(sortedList)

            favouriteAdapter.notifyDataSetChanged()
        }
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
