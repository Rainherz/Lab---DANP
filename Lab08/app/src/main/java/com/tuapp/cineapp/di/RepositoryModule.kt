package com.tuapp.cineapp.di

import com.tuapp.cineapp.data.local.dao.MovieDao
import com.tuapp.cineapp.data.remote.TmdbApiService
import com.tuapp.cineapp.data.repository.FakeMovieRepository
import com.tuapp.cineapp.data.repository.RealMovieRepository
import com.tuapp.cineapp.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        api: TmdbApiService,
        @Named("apiKey") apiKey: String,
        movieDao: MovieDao
    ): MovieRepository {
        return if (apiKey.isBlank() || apiKey == "TU_API_KEY_AQUI") {
            FakeMovieRepository()
        } else {
            RealMovieRepository(api, apiKey, movieDao)
        }
    }
}
