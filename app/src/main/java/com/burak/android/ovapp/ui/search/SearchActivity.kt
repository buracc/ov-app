package com.burak.android.ovapp.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burak.android.ovapp.R
import com.burak.android.ovapp.model.favourites.Favourite
import com.burak.android.ovapp.model.trips.Fare
import com.burak.android.ovapp.model.trips.Trip
import com.burak.android.ovapp.model.trips.adapters.TripAdapter
import com.burak.android.ovapp.ui.search.trip.TripActivity
import com.burak.android.ovapp.util.NSApi
import com.burak.android.ovapp.util.Routing
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.search_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.util.ArrayList

@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity() {

    private lateinit var from: String
    private lateinit var to: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_main)

        if (intent.extras != null) {
            if (intent.hasExtra("trips")) {
                val tripsParcelable = intent.getParcelableArrayListExtra<Trip>("trips").toList()
                rvSearch.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                rvSearch.adapter = TripAdapter(tripsParcelable) {
                    GlobalScope.launch {
                        val intent = Intent(this@SearchActivity, TripActivity::class.java)
                        intent.putExtra("trip", it)
                        startActivity(intent)
                    }
                }
            }

            if (intent.hasExtra("from")
                && intent.hasExtra("to")
                && intent.hasExtra("dateTime")
            ) {
                val title = "${intent.getStringExtra("from")} -> " +
                        intent.getStringExtra("to")
                from = intent.getStringExtra("from")!!
                to = intent.getStringExtra("to")!!
                tvSearchTitle.text = title
                tvSearchDate.text = intent.getStringExtra("dateTime")
            }

            val tripsViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

            btnFavourite.setOnClickListener {
                val favourite = Favourite(from, to)
                tripsViewModel.insertFavourite(favourite)
                Snackbar.make(rvSearch, "Added: $favourite to favourites!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}