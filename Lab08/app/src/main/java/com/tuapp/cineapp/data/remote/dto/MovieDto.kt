package com.tuapp.cineapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GenreDto(
    val id: Int,
    val name: String
)

data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    val genres: List<GenreDto>?
)
