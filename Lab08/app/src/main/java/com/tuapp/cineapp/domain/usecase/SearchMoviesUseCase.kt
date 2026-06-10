package com.tuapp.cineapp.domain.usecase

import com.tuapp.cineapp.domain.model.Movie
import com.tuapp.cineapp.domain.repository.MovieRepository
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(query: String): Result<List<Movie>> {
        if (query.isBlank()) return Result.success(emptyList())
        return repository.searchMovies(query.trim())
    }
}
