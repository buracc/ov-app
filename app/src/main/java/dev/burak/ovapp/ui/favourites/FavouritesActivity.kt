package dev.burak.ovapp.ui.favourites

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.burak.ovapp.R
import dev.burak.ovapp.exception.StationNotFoundException
import dev.burak.ovapp.model.favourites.Favourite
import dev.burak.ovapp.model.favourites.adapters.FavouriteAdapter
import dev.burak.ovapp.ui.search.SearchActivity
import dev.burak.ovapp.util.DateUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.burak.ovapp.model.trips.Trip
import dev.burak.ovapp.util.OvApi
import kotlinx.android.synthetic.main.favourites_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class FavouritesActivity : AppCompatActivity() {
    @Inject
    lateinit var ovApi: OvApi

    private lateinit var favouritesViewModel: FavouritesViewModel
    private val favourites = mutableListOf<Favourite>()
    private val favouriteAdapter = FavouriteAdapter(favourites) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val stations = ovApi.getStations().body()?.stations ?: return@launch
                val from = stations.firstOrNull { s -> s.names.values.contains(it.from) } ?: return@launch
                val to = stations.firstOrNull { s -> s.names.values.contains(it.to) } ?: return@launch
                val dateTime = OffsetDateTime.now().toZonedDateTime().toString()
                val trips = ovApi.getTrips(from.uicCode, to.uicCode, dateTime).body()?.trips ?: return@launch
                val arraylist = arrayListOf<Trip>().also {
                    trips.forEach { t -> it.add(t) }
                }
                startActivity(
                    Intent(this@FavouritesActivity, SearchActivity::class.java)
                        .putParcelableArrayListExtra("trips", arraylist)
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
