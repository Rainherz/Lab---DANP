package com.tuapp.cineapp.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tuapp.cineapp.ui.components.EmptyMessage
import com.tuapp.cineapp.ui.components.ErrorMessage
import com.tuapp.cineapp.ui.components.MovieGrid
import com.tuapp.cineapp.ui.home.MovieUiState

@Composable
fun FavoritesScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Películas favoritas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        when (val state = uiState) {
            is MovieUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is MovieUiState.Success -> MovieGrid(movies = state.movies, onMovieClick = onMovieClick)
            is MovieUiState.Error   -> ErrorMessage(state.message) { viewModel.loadFavorites() }
            is MovieUiState.Empty   -> EmptyMessage("No tienes películas favoritas guardadas")
        }
    }
}
