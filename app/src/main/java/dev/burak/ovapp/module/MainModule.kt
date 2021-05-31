package dev.burak.ovapp.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.burak.ovapp.database.DefaultFavouriteRepository
import dev.burak.ovapp.database.FavouriteDao
import dev.burak.ovapp.database.FavouriteRepository
import dev.burak.ovapp.database.FavouriteRoomDatabase
import dev.burak.ovapp.util.web.OvApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

private const val BASE_URL = "https://gateway.apiportal.ns.nl/"

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Singleton
    @Provides
    fun provideAppProperties(
        @ApplicationContext context: Context
    ): Properties {
        val props = Properties()
        props.load(context.assets.open("application.properties"))
        return props
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        appProperties: Properties
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val request = it.request().newBuilder()
                .header("Ocp-Apim-Subscription-Key", appProperties.getProperty("ns-app-primary-api-key"))
                .build()
            it.proceed(request)
        }
        .build()

    @Singleton
    @Provides
    fun provideOvApi(
        httpClient: OkHttpClient
    ): OvApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(OvApi::class.java)

    @Singleton
    @Provides
    fun provideFavouriteRoomDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, FavouriteRoomDatabase::class.java, "FAVOURITE_DATABASE"
    ).build()

    @Singleton
    @Provides
    fun provideFavouriteDao(
        database: FavouriteRoomDatabase
    ) = database.favouriteDao()

    @Singleton
    @Provides
    fun provideFavouriteRepository(
        favouriteDao: FavouriteDao
    ): FavouriteRepository = DefaultFavouriteRepository(favouriteDao)
}
