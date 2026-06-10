package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.database.entity.UserTechnologyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserTechnologyDao {

    @Query("SELECT * FROM user_technologies ORDER BY name ASC")
    fun getUserTechnologiesFlow(): Flow<List<UserTechnologyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserTechnology(tech: UserTechnologyEntity): Long

    @Query("DELETE FROM user_technologies WHERE id = :id")
    suspend fun deleteUserTechnology(id: Long)
}
