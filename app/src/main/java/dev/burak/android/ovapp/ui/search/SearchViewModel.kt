package dev.burak.android.ovapp.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.burak.android.ovapp.database.FavouriteRepository
import dev.burak.android.ovapp.model.favourites.Favourite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val favouriteRepository = FavouriteRepository(application.applicationContext)

    fun insertFavourite(favourite: Favourite) {
        ioScope.launch {
            favouriteRepository.insertFavourite(favourite)
        }
    }
}
