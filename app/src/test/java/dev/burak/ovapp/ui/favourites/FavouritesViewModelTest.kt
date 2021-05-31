package dev.burak.ovapp.ui.favourites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import dev.burak.ovapp.MainCoroutineRule
import dev.burak.ovapp.getOrAwaitValueTest
import dev.burak.ovapp.model.Favourite
import dev.burak.ovapp.repository.FakeFavouriteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavouritesViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: FavouritesViewModel
    private val favourite = Favourite("Hilversum", "Amsterdam", id = 1)

    @Before
    fun setUp() {
        viewModel = FavouritesViewModel(FakeFavouriteRepository())
    }

    @Test
    fun testInsert() {
        viewModel.insertFavourite(favourite)
        val all = viewModel.favouriteRepository.getAllFavourites().getOrAwaitValueTest()

        assertThat(all).contains(favourite)
    }
}
