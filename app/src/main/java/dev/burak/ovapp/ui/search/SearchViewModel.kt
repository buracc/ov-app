package dev.burak.ovapp.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.burak.ovapp.database.FavouriteRepository
import dev.burak.ovapp.model.Favourite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val favouriteRepository: FavouriteRepository,
    application: Application
) : AndroidViewModel(application) {
    private val ioScope = CoroutineScope(Dispatchers.IO)

    fun insertFavourite(favourite: Favourite) {
        ioScope.launch {
            favouriteRepository.insertFavourite(favourite)
        }
    }
}
