package com.tuapp.cineapp.domain.usecase

import com.tuapp.cineapp.domain.model.Movie
import com.tuapp.cineapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): Result<List<Movie>> =
        repository.getPopularMovies()
}
