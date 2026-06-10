package com.tuapp.cineapp.domain.usecase

import com.tuapp.cineapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieTrailerUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Result<String?> =
        repository.getMovieTrailerKey(movieId)
}
