package com.tuapp.cineapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuapp.cineapp.domain.usecase.SearchMoviesUseCase
import com.tuapp.cineapp.ui.home.MovieUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(kotlinx.coroutines.FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMovies: SearchMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Empty)
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    init {
        viewModelScope.launch {
            _query
                .debounce(400)
                .filter { it.isNotBlank() }
                .collectLatest { q ->
                    _uiState.value = MovieUiState.Loading
                    searchMovies(q)
                        .onSuccess { _uiState.value = if (it.isEmpty()) MovieUiState.Empty else MovieUiState.Success(it) }
                        .onFailure { _uiState.value = MovieUiState.Error(it.message ?: "Error") }
                }
        }
    }

    fun onQueryChange(q: String) {
        _query.value = q
        if (q.isBlank()) _uiState.value = MovieUiState.Empty
    }
}
