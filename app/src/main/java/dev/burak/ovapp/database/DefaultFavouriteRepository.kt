package dev.burak.ovapp.database

import androidx.lifecycle.LiveData
import dev.burak.ovapp.model.Favourite
import javax.inject.Inject

class DefaultFavouriteRepository @Inject constructor(val favouriteDao: FavouriteDao) : FavouriteRepository {
    override fun getAllFavourites(): LiveData<List<Favourite>> {
        return favouriteDao.getAllFavourites()
    }

    override suspend fun insertFavourite(favourite: Favourite) {
        favouriteDao.insertFavourite(favourite)
    }

    override suspend fun deleteFavourite(favourite: Favourite) = favouriteDao.deleteFavourite(favourite)

    override suspend fun deleteAllFavourites() = favouriteDao.deleteAllFavourites()
}
