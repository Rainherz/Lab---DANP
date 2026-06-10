package com.example.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.database.dao.UserTechnologyDao
import com.example.core.database.dao.VulnerabilityDao
import com.example.core.database.entity.UserTechnologyEntity
import com.example.core.database.entity.VulnerabilityEntity


@Database(
    entities = [VulnerabilityEntity::class, UserTechnologyEntity::class],
    version = 4,
    exportSchema = false
)
abstract class SecAlertsDatabase : RoomDatabase() {
    abstract fun vulnerabilityDao(): VulnerabilityDao
    abstract fun userTechnologyDao(): UserTechnologyDao
}

