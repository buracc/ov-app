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

    suspend fun insertFavourite(game: Favourite) {
        favouriteDao.insertFavourite(game)
    }

    suspend fun deleteFavourite(game: Favourite) = favouriteDao.deleteFavourite(game)

    suspend fun deleteAllFavourites() = favouriteDao.deleteAllFavourites()
}
