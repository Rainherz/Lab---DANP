package com.tuapp.cineapp.data.repository

import com.tuapp.cineapp.data.local.dao.MovieDao
import com.tuapp.cineapp.data.local.entity.MovieEntity
import com.tuapp.cineapp.data.remote.TmdbApiService
import com.tuapp.cineapp.data.remote.dto.MovieDto
import com.tuapp.cineapp.domain.model.Movie
import com.tuapp.cineapp.domain.repository.MovieRepository
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class RealMovieRepository @Inject constructor(
    private val api: TmdbApiService,
    @Named("apiKey") private val apiKey: String,
    private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun getPopularMovies(): Result<List<Movie>> = runCatching {
        api.getPopularMovies(apiKey).results.map { it.toDomain() }
    }

    override fun getPopularMoviesPaging(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(api, apiKey) }
        ).flow
    }

    override suspend fun searchMovies(query: String): Result<List<Movie>> = runCatching {
        api.searchMovies(apiKey, query).results.map { it.toDomain() }
    }

    override suspend fun getMovieDetail(id: Int): Result<Movie> = runCatching {
        api.getMovieDetail(id, apiKey).toDomain()
    }

    override suspend fun getMovieTrailerKey(movieId: Int): Result<String?> = runCatching {
        val response = api.getMovieVideos(movieId, apiKey)
        val videos = response.results
        val trailer = videos.find { it.site.equals("YouTube", ignoreCase = true) && it.type.equals("Trailer", ignoreCase = true) && it.official }
            ?: videos.find { it.site.equals("YouTube", ignoreCase = true) && it.type.equals("Trailer", ignoreCase = true) }
            ?: videos.find { it.site.equals("YouTube", ignoreCase = true) }
        trailer?.key
    }

    override suspend fun getFavorites(): List<Movie> {
        return movieDao.getFavoriteMovies().map { it.toDomain() }
    }

    override suspend fun toggleFavorite(movie: Movie) {
        val entity = MovieEntity.fromDomain(movie)
        if (movieDao.isFavorite(movie.id)) {
            movieDao.deleteFavorite(entity)
        } else {
            movieDao.insertFavorite(entity)
        }
    }

    override suspend fun isFavorite(movieId: Int): Boolean {
        return movieDao.isFavorite(movieId)
    }
}

fun MovieDto.toDomain() = Movie(
    id = id,
    title = title,
    overview = overview,
    posterUrl = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
    backdropUrl = backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" } ?: "",
    rating = voteAverage,
    releaseDate = releaseDate,
    genreIds = genreIds ?: genres?.map { it.id } ?: emptyList()
)
