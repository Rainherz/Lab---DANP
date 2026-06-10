package com.tuapp.cineapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.tuapp.cineapp.data.repository.FakeMovieRepository
import com.tuapp.cineapp.domain.usecase.GetMovieDetailUseCase
import com.tuapp.cineapp.domain.usecase.ToggleFavoriteUseCase
import com.tuapp.cineapp.ui.home.MovieUiState
import com.tuapp.cineapp.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepo: FakeMovieRepository
    private lateinit var getMovieDetail: GetMovieDetailUseCase
    private lateinit var toggleFavorite: ToggleFavoriteUseCase

    @Before
    fun setup() {
        fakeRepo = FakeMovieRepository()
        getMovieDetail = GetMovieDetailUseCase(fakeRepo)
        toggleFavorite = ToggleFavoriteUseCase(fakeRepo)
    }

    @Test
    fun `carga detalle de pelicula exitosamente`() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf("movieId" to 1))
        val viewModel = DetailViewModel(getMovieDetail, toggleFavorite, savedStateHandle)

        viewModel.uiState.test {
            val first = awaitItem()
            val second = if (first is MovieUiState.Loading) awaitItem() else first
            assert(second is MovieUiState.Success)
            val success = second as MovieUiState.Success
            assertEquals(1, success.movies.first().id)
            assertEquals("Dune: Parte Dos", success.movies.first().title)
        }
    }

    @Test
    fun `maneja error de carga de pelicula`() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf("movieId" to 999))
        val viewModel = DetailViewModel(getMovieDetail, toggleFavorite, savedStateHandle)

        viewModel.uiState.test {
            val first = awaitItem()
            val second = if (first is MovieUiState.Loading) awaitItem() else first
            assert(second is MovieUiState.Error)
            assertEquals("Película no encontrada", (second as MovieUiState.Error).message)
        }
    }

    @Test
    fun `cambia de favorito al invocar toggleFavorite`() = runTest {
        val movie = fakeRepo.fakeMovies.first()
        val savedStateHandle = SavedStateHandle(mapOf("movieId" to movie.id))
        val viewModel = DetailViewModel(getMovieDetail, toggleFavorite, savedStateHandle)

        viewModel.isFavorite.test {
            assertEquals(false, awaitItem())
            viewModel.toggleFavorite(movie)
            assertEquals(true, awaitItem())
            viewModel.toggleFavorite(movie)
            assertEquals(false, awaitItem())
        }
    }
}
