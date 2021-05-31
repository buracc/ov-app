package dev.burak.ovapp.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.burak.ovapp.database.FavouriteRoomDatabase
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestModule {
    @Provides
    @Named("testDb")
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ) = Room.inMemoryDatabaseBuilder(context, FavouriteRoomDatabase::class.java)
        .allowMainThreadQueries()
        .build()
}
