package com.tuapp.cineapp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuapp.cineapp.domain.usecase.ToggleFavoriteUseCase
import com.tuapp.cineapp.ui.home.MovieUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = MovieUiState.Loading
            val favorites = toggleFavorite.getFavorites()
            _uiState.value = if (favorites.isEmpty()) MovieUiState.Empty
                             else MovieUiState.Success(favorites)
        }
    }
}
