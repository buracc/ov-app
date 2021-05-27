package dev.burak.ovapp.ui.favourites

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.burak.ovapp.R
import dev.burak.ovapp.model.Favourite
import dev.burak.ovapp.adapter.FavouriteAdapter
import dev.burak.ovapp.ui.search.SearchActivity
import dev.burak.ovapp.util.DateUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.burak.ovapp.model.Trip
import dev.burak.ovapp.util.web.OvApi
import kotlinx.android.synthetic.main.favourites_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.DateTimeException
import java.time.OffsetDateTime
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class FavouritesActivity : AppCompatActivity() {
    @Inject
    lateinit var ovApi: OvApi
    val favouritesViewModel: FavouritesViewModel by lazy {
        ViewModelProvider(this).get(FavouritesViewModel::class.java)
    }

    private val favourites = mutableListOf<Favourite>()
    private val favouriteAdapter = FavouriteAdapter(favourites) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val stations = ovApi.getStations().body()?.stations ?: return@launch
                val from = stations.firstOrNull { s -> s.names.values.contains(it.from) } ?: return@launch
                val to = stations.firstOrNull { s -> s.names.values.contains(it.to) } ?: return@launch
                val dateTime = OffsetDateTime.now().toZonedDateTime().toString()
                val trips = ovApi.getTrips(from.uicCode, to.uicCode, dateTime).body()?.trips ?: return@launch
                startActivity(
                    Intent(this@FavouritesActivity, SearchActivity::class.java)
                        .putParcelableArrayListExtra("trips", arrayListOf<Trip>().also {
                            trips.forEach { t -> it.add(t) }
                        })
                        .putExtra("from", from)
                        .putExtra("to", to)
                        .putExtra("dateTime", DateUtil.toDateTimeString(dateTime, false))
                )
            } catch (e: DateTimeException) {
                Toast.makeText(
                    this@FavouritesActivity,
                    "Invalid date entered.",
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
