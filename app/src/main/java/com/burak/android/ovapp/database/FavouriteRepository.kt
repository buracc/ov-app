package com.burak.android.ovapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.burak.android.ovapp.model.favourites.Favourite

class FavouriteRepository(context: Context) {

    private val favouriteDao: FavouriteDao

    init {
        val database = FavouriteRoomDatabase.getDatabase(context)
        favouriteDao = database!!.favouriteDao()
    }

    fun getAllFavourites(): LiveData<List<Favourite>> {
        return favouriteDao.getAllFavourites()
    }

    suspend fun insertFavourite(favourite: Favourite) {
        favouriteDao.insertFavourite(favourite)
    }

    suspend fun deleteFavourite(favourite: Favourite) = favouriteDao.deleteFavourite(favourite)

    suspend fun deleteAllFavourites() = favouriteDao.deleteAllFavourites()
}
