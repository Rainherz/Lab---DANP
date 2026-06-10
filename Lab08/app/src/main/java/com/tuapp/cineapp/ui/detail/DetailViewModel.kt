package com.tuapp.cineapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuapp.cineapp.domain.model.Movie
import com.tuapp.cineapp.domain.usecase.GetMovieDetailUseCase
import com.tuapp.cineapp.domain.usecase.ToggleFavoriteUseCase
import com.tuapp.cineapp.ui.home.MovieUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetail: GetMovieDetailUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    init {
        viewModelScope.launch {
            getMovieDetail(movieId)
                .onSuccess { movie ->
                    _uiState.value = MovieUiState.Success(listOf(movie))
                    _isFavorite.value = toggleFavorite.isFavorite(movie.id)
                }
                .onFailure { _uiState.value = MovieUiState.Error(it.message ?: "Error") }
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            toggleFavorite.invoke(movie)
            _isFavorite.value = toggleFavorite.isFavorite(movie.id)
        }
    }
}
