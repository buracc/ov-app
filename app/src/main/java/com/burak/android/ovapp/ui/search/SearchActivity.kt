package com.burak.android.ovapp.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burak.android.ovapp.R
import com.burak.android.ovapp.model.favourites.Favourite
import com.burak.android.ovapp.model.trips.Trip
import com.burak.android.ovapp.model.trips.adapters.TripAdapter
import com.burak.android.ovapp.ui.search.trip.TripActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.search_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity() {

    private lateinit var from: String
    private lateinit var to: String

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

                val fromExtra = extras.getString("from")
                val toExtra = extras.getString("to")
                val dateTimeExtra = extras.getString("dateTime")

                if (fromExtra != null && toExtra != null && dateTimeExtra != null) {
                    from = fromExtra
                    to = toExtra
                    tvSearchTitle.text = "$from -> $to"
                    tvSearchDate.text = dateTimeExtra
                }

                val tripsViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

                btnFavourite.setOnClickListener {
                    val favourite = Favourite(from, to)
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