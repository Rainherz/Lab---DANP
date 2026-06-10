package com.tuapp.cineapp.domain.repository

import androidx.paging.PagingData
import com.tuapp.cineapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(): Result<List<Movie>>
    fun getPopularMoviesPaging(): Flow<PagingData<Movie>>
    suspend fun searchMovies(query: String): Result<List<Movie>>
    suspend fun getMovieDetail(id: Int): Result<Movie>
    suspend fun getMovieTrailerKey(movieId: Int): Result<String?>
    suspend fun getFavorites(): List<Movie>
    suspend fun toggleFavorite(movie: Movie)
    suspend fun isFavorite(movieId: Int): Boolean
}
