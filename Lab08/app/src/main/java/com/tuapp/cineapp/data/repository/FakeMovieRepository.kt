package com.tuapp.cineapp.data.repository

import androidx.paging.PagingData
import com.tuapp.cineapp.domain.model.Movie
import com.tuapp.cineapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMovieRepository : MovieRepository {

    private val favorites = mutableSetOf<Movie>()

    val fakeMovies = listOf(
        Movie(1, "Dune: Parte Dos", "Paul Atreides une fuerzas...", "https://picsum.photos/seed/dune/300/450", "", 8.4, "2024-03-01", listOf(878, 12)),
        Movie(2, "Oppenheimer", "La historia del padre de la bomba atómica.", "https://picsum.photos/seed/oppe/300/450", "", 8.6, "2023-07-21", listOf(18, 36)),
        Movie(3, "Poor Things", "La increíble historia de Bella Baxter.", "https://picsum.photos/seed/poor/300/450", "", 8.0, "2024-01-12", listOf(35, 18)),
        Movie(4, "Furiosa", "El origen de Mad Max Fury Road.", "https://picsum.photos/seed/furi/300/450", "", 7.5, "2024-05-24", listOf(28, 12)),
        Movie(5, "Inside Out 2", "Riley enfrenta nuevas emociones en la adolescencia.", "https://picsum.photos/seed/inside/300/450", "", 7.9, "2024-06-14", listOf(16, 18)),
        Movie(6, "Alien: Romulus", "Un grupo de jóvenes colonizadores enfrenta a los xenomorfos.", "https://picsum.photos/seed/alien/300/450", "", 7.3, "2024-08-16", listOf(27, 878))
    )

    var shouldReturnError = false

    override suspend fun getPopularMovies(): Result<List<Movie>> {
        if (shouldReturnError) return Result.failure(Exception("Error de red simulado"))
        return Result.success(fakeMovies)
    }

    override fun getPopularMoviesPaging(): Flow<PagingData<Movie>> {
        return flowOf(PagingData.from(fakeMovies))
    }

    override suspend fun searchMovies(query: String): Result<List<Movie>> {
        if (shouldReturnError) return Result.failure(Exception("Error de red simulado"))
        val filtered = fakeMovies.filter { it.title.contains(query, ignoreCase = true) }
        return Result.success(filtered)
    }

    override suspend fun getMovieDetail(id: Int): Result<Movie> {
        val movie = fakeMovies.find { it.id == id }
            ?: return Result.failure(Exception("Película no encontrada"))
        return Result.success(movie)
    }

    override suspend fun getMovieTrailerKey(movieId: Int): Result<String?> {
        if (shouldReturnError) return Result.failure(Exception("Error de red simulado"))
        return Result.success("d9MyW72ELq0")
    }

    override suspend fun getFavorites(): List<Movie> = favorites.toList()

    override suspend fun toggleFavorite(movie: Movie) {
        if (favorites.any { it.id == movie.id }) favorites.removeIf { it.id == movie.id }
        else favorites.add(movie)
    }

    override suspend fun isFavorite(movieId: Int): Boolean = favorites.any { it.id == movieId }
}
