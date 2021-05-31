package dev.burak.ovapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.burak.ovapp.database.FavouriteRepository
import dev.burak.ovapp.model.Favourite

class FakeFavouriteRepository : FavouriteRepository {
    private val items = mutableListOf<Favourite>()
    private val favourites = MutableLiveData<List<Favourite>>(items)

    private fun refresh() {
        favourites.postValue(items)
    }

    override fun getAllFavourites(): LiveData<List<Favourite>> {
        return favourites
    }

    override suspend fun insertFavourite(favourite: Favourite) {
        items.add(favourite)
        refresh()
    }

    override suspend fun deleteFavourite(favourite: Favourite) {
        items.remove(favourite)
        refresh()
    }

    override suspend fun deleteAllFavourites() {
        items.clear()
        refresh()
    }
}
