package com.example.core.database.di

import android.content.Context
import androidx.room.Room
import com.example.core.database.SecAlertsDatabase
import com.example.core.database.dao.UserTechnologyDao
import com.example.core.database.dao.VulnerabilityDao
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
    fun provideDatabase(@ApplicationContext context: Context): SecAlertsDatabase {
        return Room.databaseBuilder(
            context,
            SecAlertsDatabase::class.java,
            "secalerts_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideVulnerabilityDao(database: SecAlertsDatabase): VulnerabilityDao {
        return database.vulnerabilityDao()
    }

    @Provides
    @Singleton
    fun provideUserTechnologyDao(database: SecAlertsDatabase): UserTechnologyDao {
        return database.userTechnologyDao()
    }
}

