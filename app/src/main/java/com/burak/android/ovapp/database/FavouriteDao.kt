package com.burak.android.ovapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.burak.android.ovapp.model.favourites.Favourite

@Dao
interface FavouriteDao {
    
    @Insert
    suspend fun insertFavourite(favourite: Favourite)

    @Query("SELECT * FROM Favourite")
    fun getAllFavourites(): LiveData<List<Favourite>>

    @Delete
    suspend fun deleteFavourite(favourite: Favourite)

    @Query("DELETE FROM Favourite")
    suspend fun deleteAllFavourites()

}