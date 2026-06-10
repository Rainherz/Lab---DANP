package com.tuapp.cineapp.ui.home

import com.tuapp.cineapp.domain.model.Movie

sealed class MovieUiState {
    object Loading : MovieUiState()
    data class Success(val movies: List<Movie>) : MovieUiState()
    data class Error(val message: String) : MovieUiState()
    object Empty : MovieUiState()
}
