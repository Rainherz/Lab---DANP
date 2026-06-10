package com.tuapp.cineapp.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val rating: Double,
    val releaseDate: String,
    val genreIds: List<Int>
) {
    val releaseYear: String
        get() = if (releaseDate.length >= 4) releaseDate.take(4) else releaseDate

    val formattedRating: String
        get() = try {
            String.format(java.util.Locale.US, "%.1f", rating)
        } catch (e: Exception) {
            rating.toString()
        }
}
