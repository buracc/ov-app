package dev.burak.ovapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.burak.ovapp.model.favourites.Favourite

@Database(entities = [Favourite::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FavouriteRoomDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao
}
