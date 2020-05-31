package com.burak.android.ovapp.ui.trips

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burak.android.ovapp.R
import com.burak.android.ovapp.model.favourites.Favourite
import com.burak.android.ovapp.model.trips.Trip
import com.burak.android.ovapp.model.trips.adapter.TripAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favourites_main.*
import kotlinx.android.synthetic.main.trip_card.*
import kotlinx.android.synthetic.main.trips_main.*

class TripsActivity : AppCompatActivity() {

    private lateinit var tripAdapter: TripAdapter

    private lateinit var from: String
    private lateinit var to: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trips_main)

        if (intent.extras != null) {
            if (intent.hasExtra("trips")) {
                val tripsParcelable = intent.getParcelableArrayListExtra<Trip>("trips").toList()
                tripAdapter = TripAdapter(tripsParcelable)
                rvTrips.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                rvTrips.adapter = tripAdapter
            }

            if (intent.hasExtra("from") && intent.hasExtra("to")) {
                val title = "${intent.extras!!.getString("from")} -> " +
                        "${intent.extras!!.getString("to")}"
                from = intent.extras!!.getString("from")!!
                to = intent.extras!!.getString("to")!!
                tvTripsTitle.text = title
            }

            val tripsViewModel = ViewModelProviders.of(this).get(TripsViewModel::class.java)

            btnFavourite.setOnClickListener {
                val favourite = Favourite(from, to)
                tripsViewModel.insertFavourite(favourite)
                Snackbar.make(rvTrips, "Added: $favourite to favourites!", Snackbar.LENGTH_SHORT).show()
            }

        }
    }
}