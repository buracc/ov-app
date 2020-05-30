package com.burak.android.ovapp.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

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
                        FavouriteRoomDatabase::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }

}