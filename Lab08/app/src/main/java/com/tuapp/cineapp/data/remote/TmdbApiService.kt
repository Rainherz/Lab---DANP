package com.tuapp.cineapp.data.remote

import com.tuapp.cineapp.data.remote.dto.MovieDto
import com.tuapp.cineapp.data.remote.dto.MovieListResponseDto
import com.tuapp.cineapp.data.remote.dto.MovieVideosResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): MovieListResponseDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): MovieListResponseDto

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): MovieDto

    @GET("movie/{id}/videos")
    suspend fun getMovieVideos(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): MovieVideosResponseDto
}
