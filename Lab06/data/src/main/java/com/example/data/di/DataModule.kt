package com.example.data.di

import com.example.data.repository.AlertRepositoryImpl
import com.example.domain.repository.AlertRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindAlertRepository(
        alertRepositoryImpl: AlertRepositoryImpl
    ): AlertRepository
}
