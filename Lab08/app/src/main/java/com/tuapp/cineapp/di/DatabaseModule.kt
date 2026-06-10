package com.tuapp.cineapp.di

import android.content.Context
import androidx.room.Room
import com.tuapp.cineapp.data.local.CineDatabase
import com.tuapp.cineapp.data.local.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCineDatabase(
        @ApplicationContext context: Context
    ): CineDatabase {
        return Room.databaseBuilder(
            context,
            CineDatabase::class.java,
            "cine_database.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(db: CineDatabase): MovieDao {
        return db.movieDao
    }
}
