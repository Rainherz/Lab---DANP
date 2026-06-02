package com.example.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_technologies")
data class UserTechnologyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val version: String
)
