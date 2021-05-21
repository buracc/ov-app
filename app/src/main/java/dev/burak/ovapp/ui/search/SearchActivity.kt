package dev.burak.ovapp.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.burak.ovapp.R
import dev.burak.ovapp.model.favourites.Favourite
import dev.burak.ovapp.model.trips.adapters.TripAdapter
import dev.burak.ovapp.ui.search.trip.TripActivity
import com.google.android.material.snackbar.Snackbar
import dev.burak.ovapp.model.stations.Station
import dev.burak.ovapp.model.trips.Trip
import kotlinx.android.synthetic.main.search_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity() {

    private lateinit var from: Station
    private lateinit var to: Station

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_main)

        val extras = intent.extras
        if (extras != null) {
            val tripsExtra = extras.getParcelableArrayList<Trip>("trips")

            if (tripsExtra != null) {
                rvSearch.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                rvSearch.adapter = TripAdapter(tripsExtra.toList()) {
                    GlobalScope.launch {
                        val intent = Intent(this@SearchActivity, TripActivity::class.java)
                            .putExtra("trip", it)
                        startActivity(intent)
                    }
                }

                val fromExtra = extras.getParcelable<Station>("from")
                val toExtra = extras.getParcelable<Station>("to")
                val dateTimeExtra = extras.getString("dateTime")

                if (fromExtra != null && toExtra != null && dateTimeExtra != null) {
                    from = fromExtra
                    to = toExtra
                    tvSearchTitle.text = "$from -> $to"
                    tvSearchDate.text = dateTimeExtra
                }

                val tripsViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

                btnFavourite.setOnClickListener {
                    val favourite = Favourite(from.name, to.name)
                    tripsViewModel.insertFavourite(favourite)
                    Snackbar.make(
                        rvSearch,
                        "Added: $favourite to favourites!",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }
}
