package com.tuapp.cineapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tuapp.cineapp.domain.model.Movie

@Entity(tableName = "favorite_movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val rating: Double,
    val releaseDate: String,
    val genreIds: String
) {
    fun toDomain(): Movie {
        val ids = if (genreIds.isBlank()) emptyList() else genreIds.split(",").mapNotNull { it.toIntOrNull() }
        return Movie(
            id = id,
            title = title,
            overview = overview,
            posterUrl = posterUrl,
            backdropUrl = backdropUrl,
            rating = rating,
            releaseDate = releaseDate,
            genreIds = ids
        )
    }

    companion object {
        fun fromDomain(movie: Movie): MovieEntity {
            return MovieEntity(
                id = movie.id,
                title = movie.title,
                overview = movie.overview,
                posterUrl = movie.posterUrl,
                backdropUrl = movie.backdropUrl,
                rating = movie.rating,
                releaseDate = movie.releaseDate,
                genreIds = movie.genreIds.joinToString(",")
            )
        }
    }
}
