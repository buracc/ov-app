package dev.burak.ovapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.burak.ovapp.model.favourites.Favourite

@Database(entities = [Favourite::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FavouriteRoomDatabase : RoomDatabase() {

    abstract fun favouriteDao(): FavouriteDao

    companion object {
        private const val DATABASE_NAME = "FAVOURITE_DATABASE"

        @Volatile
        private var INSTANCE: FavouriteRoomDatabase? = null

        fun getDatabase(context: Context): FavouriteRoomDatabase? {
            synchronized(FavouriteRoomDatabase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavouriteRoomDatabase::class.java, DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
