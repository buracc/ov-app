package com.burak.android.ovapp.ui.favourites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.burak.android.ovapp.database.FavouriteRepository
import com.burak.android.ovapp.model.favourites.Favourite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouritesViewModel(application: Application) : AndroidViewModel(application) {

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val favouriteRepository = FavouriteRepository(application.applicationContext)

    val favourites: LiveData<List<Favourite>> = favouriteRepository.getAllFavourites()

    fun deleteFavourite(favourite: Favourite) {
        ioScope.launch {
            favouriteRepository.deleteFavourite(favourite)
        }
    }

    fun deleteAllFavourites() {
        ioScope.launch {
            favouriteRepository.deleteAllFavourites()
        }
    }
}