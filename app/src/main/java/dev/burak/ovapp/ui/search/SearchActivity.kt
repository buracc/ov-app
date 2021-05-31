package dev.burak.ovapp.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.burak.ovapp.R
import dev.burak.ovapp.adapter.TripAdapter
import dev.burak.ovapp.model.Favourite
import dev.burak.ovapp.model.Station
import dev.burak.ovapp.model.Trip
import dev.burak.ovapp.ui.trip.TripActivity
import dev.burak.ovapp.util.FormatUtils
import kotlinx.android.synthetic.main.search_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var from: Station
    private lateinit var to: Station

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

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
                val dateTimeExtra = extras.get("dateTime") as ZonedDateTime?

                if (fromExtra != null && toExtra != null && dateTimeExtra != null) {
                    from = fromExtra
                    to = toExtra
                    tvSearchTitle.text = "$from -> $to"
                    tvSearchDate.text = FormatUtils.dateAndTimeFormatter.format(dateTimeExtra)
                }

                btnFavourite.setOnClickListener {
                    val favourite = Favourite(from.name, to.name)
                    searchViewModel.insertFavourite(favourite)
                    Snackbar.make(
                        rvSearch,
                        "Added: $favourite to favourites!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
