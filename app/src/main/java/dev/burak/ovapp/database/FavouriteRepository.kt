package dev.burak.ovapp.database

import androidx.lifecycle.LiveData
import dev.burak.ovapp.model.Favourite
import javax.inject.Inject

class FavouriteRepository @Inject constructor(val favouriteDao: FavouriteDao) {
    fun getAllFavourites(): LiveData<List<Favourite>> {
        return favouriteDao.getAllFavourites()
    }

    suspend fun insertFavourite(favourite: Favourite) {
        favouriteDao.insertFavourite(favourite)
    }

    suspend fun deleteFavourite(favourite: Favourite) = favouriteDao.deleteFavourite(favourite)

    suspend fun deleteAllFavourites() = favouriteDao.deleteAllFavourites()
}
