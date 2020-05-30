package com.burak.android.ovapp.database

import androidx.room.RoomDatabase
import com.burak.android.ovapp.database.dao.OVDao

abstract class OVDatabase : RoomDatabase() {

    abstract fun ovDao(): OVDao
}