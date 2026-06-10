package com.tuapp.cineapp.domain.usecase

import com.tuapp.cineapp.domain.model.Movie
import com.tuapp.cineapp.domain.repository.MovieRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) = repository.toggleFavorite(movie)
    suspend fun isFavorite(movieId: Int) = repository.isFavorite(movieId)
    suspend fun getFavorites() = repository.getFavorites()
}
