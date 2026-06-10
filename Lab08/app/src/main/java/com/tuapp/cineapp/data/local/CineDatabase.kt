package com.tuapp.cineapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tuapp.cineapp.data.local.dao.MovieDao
import com.tuapp.cineapp.data.local.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class CineDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
}
