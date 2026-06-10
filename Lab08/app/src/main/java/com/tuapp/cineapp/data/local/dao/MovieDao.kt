package com.tuapp.cineapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuapp.cineapp.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM favorite_movies")
    fun getFavoriteMoviesFlow(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM favorite_movies")
    suspend fun getFavoriteMovies(): List<MovieEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId LIMIT 1)")
    suspend fun isFavorite(movieId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: MovieEntity): Long

    @Delete
    suspend fun deleteFavorite(movie: MovieEntity): Int
}
