package com.tuapp.cineapp.domain.usecase

import androidx.paging.PagingData
import com.tuapp.cineapp.domain.model.Movie
import com.tuapp.cineapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularMoviesPagingUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<PagingData<Movie>> {
        return repository.getPopularMoviesPaging()
    }
}
