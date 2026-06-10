package com.tuapp.cineapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.tuapp.cineapp.domain.model.Genre
import com.tuapp.cineapp.ui.components.EmptyMessage
import com.tuapp.cineapp.ui.components.ErrorMessage
import com.tuapp.cineapp.ui.components.MoviePagingGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val selectedGenre by viewModel.selectedGenre.collectAsStateWithLifecycle()
    val movies = viewModel.moviesFlow.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Películas populares",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        )

        LazyRow(
            modifier = Modifier.padding(bottom = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(Genre.values()) { genre ->
                FilterChip(
                    selected = selectedGenre == genre,
                    onClick = { viewModel.onGenreSelected(genre) },
                    label = { Text(genre.displayName) }
                )
            }
        }

        when {
            movies.loadState.refresh is LoadState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            movies.loadState.refresh is LoadState.Error -> {
                val error = movies.loadState.refresh as LoadState.Error
                ErrorMessage(error.error.message ?: "Error desconocido") {
                    movies.retry()
                }
            }
            movies.loadState.refresh is LoadState.NotLoading && movies.itemCount == 0 -> {
                EmptyMessage("No hay películas disponibles para esta categoría")
            }
            else -> {
                MoviePagingGrid(movies = movies, onMovieClick = onMovieClick)
            }
        }
    }
}
