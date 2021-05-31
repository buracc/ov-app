package dev.burak.ovapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.burak.ovapp.getOrAwaitValue
import dev.burak.ovapp.model.Favourite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class FavouriteDaoTest {
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("testDb")
    lateinit var database: FavouriteRoomDatabase

    private lateinit var dao: FavouriteDao
    private val favourite = Favourite("Hilversum", "Amsterdam", id = 1)

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        dao = database.favouriteDao()
    }

    @Test
    fun testInsert() = runBlockingTest {
        dao.insertFavourite(favourite)
        val all = dao.getAllFavourites().getOrAwaitValue()
        assertThat(all).contains(favourite)
    }

    @Test
    fun testDelete() = runBlockingTest {
        dao.insertFavourite(favourite)
        dao.deleteFavourite(favourite)
        val all = dao.getAllFavourites().getOrAwaitValue()
        assertThat(all).doesNotContain(favourite)
    }

    @After
    fun tearDown() {
        database.close()
    }
}
