package dev.burak.ovapp.database

import androidx.lifecycle.LiveData
import dev.burak.ovapp.model.Favourite
import javax.inject.Inject

interface FavouriteRepository {
    fun getAllFavourites(): LiveData<List<Favourite>>

    suspend fun insertFavourite(favourite: Favourite)

    suspend fun deleteFavourite(favourite: Favourite)

    suspend fun deleteAllFavourites()
}
