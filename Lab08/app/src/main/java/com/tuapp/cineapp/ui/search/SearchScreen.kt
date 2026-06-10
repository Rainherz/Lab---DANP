package com.tuapp.cineapp.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun SearchScreen(onMovieClick: (Int) -> Unit, viewModel: SearchViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onQueryChange,
            placeholder = { Text("Buscar películas...") },
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) IconButton(onClick = { viewModel.onQueryChange("") }) {
                    Icon(Icons.Rounded.Close, contentDescription = "Limpiar")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        when (val state = uiState) {
            is MovieUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is MovieUiState.Success -> MovieGrid(movies = state.movies, onMovieClick = onMovieClick)
            is MovieUiState.Error   -> ErrorMessage(state.message)
            is MovieUiState.Empty   -> EmptyMessage(if (query.isBlank()) "Escribe para buscar" else "Sin resultados para \"$query\"")
        }
    }
}
