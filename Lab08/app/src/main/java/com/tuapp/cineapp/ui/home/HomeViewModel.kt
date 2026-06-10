package com.tuapp.cineapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.tuapp.cineapp.domain.model.Genre
import com.tuapp.cineapp.domain.model.Movie
import com.tuapp.cineapp.domain.usecase.GetPopularMoviesPagingUseCase
import com.tuapp.cineapp.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesPaging: GetPopularMoviesPagingUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {

    private val _selectedGenre = MutableStateFlow(Genre.ALL)
    val selectedGenre: StateFlow<Genre> = _selectedGenre.asStateFlow()

    val moviesFlow: Flow<PagingData<Movie>> = _selectedGenre
        .flatMapLatest { genre ->
            getPopularMoviesPaging()
                .map { pagingData ->
                    if (genre == Genre.ALL) {
                        pagingData
                    } else {
                        pagingData.filter { movie -> movie.genreIds.contains(genre.id) }
                    }
                }
        }
        .cachedIn(viewModelScope)

    fun onGenreSelected(genre: Genre) {
        _selectedGenre.value = genre
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            toggleFavorite.invoke(movie)
        }
    }
}
