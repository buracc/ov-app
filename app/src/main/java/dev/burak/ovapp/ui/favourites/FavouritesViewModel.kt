package dev.burak.ovapp.ui.favourites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.burak.ovapp.database.FavouriteRepository
import dev.burak.ovapp.model.Favourite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    val favouriteRepository: FavouriteRepository,
    application: Application
) : AndroidViewModel(application) {
    private val ioScope = CoroutineScope(Dispatchers.IO)
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
