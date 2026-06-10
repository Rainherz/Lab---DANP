package com.tuapp.cineapp.ui.home

import app.cash.turbine.test
import com.tuapp.cineapp.data.repository.FakeMovieRepository
import com.tuapp.cineapp.domain.model.Genre
import com.tuapp.cineapp.domain.usecase.GetPopularMoviesPagingUseCase
import com.tuapp.cineapp.domain.usecase.ToggleFavoriteUseCase
import com.tuapp.cineapp.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepo: FakeMovieRepository

    @Before
    fun setup() {
        fakeRepo = FakeMovieRepository()
    }

    @Test
    fun `emite Genre ALL inicialmente`() = runTest {
        val getPopularPaging = GetPopularMoviesPagingUseCase(fakeRepo)
        val toggleFav = ToggleFavoriteUseCase(fakeRepo)
        val viewModel = HomeViewModel(getPopularPaging, toggleFav)

        viewModel.selectedGenre.test {
            assertEquals(Genre.ALL, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `cambia de genero cuando se selecciona una categoria`() = runTest {
        val getPopularPaging = GetPopularMoviesPagingUseCase(fakeRepo)
        val toggleFav = ToggleFavoriteUseCase(fakeRepo)
        val viewModel = HomeViewModel(getPopularPaging, toggleFav)

        viewModel.selectedGenre.test {
            assertEquals(Genre.ALL, awaitItem())
            viewModel.onGenreSelected(Genre.ACTION)
            assertEquals(Genre.ACTION, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
