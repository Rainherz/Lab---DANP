package com.example.domain.repository

import com.example.domain.model.UserTechnology
import com.example.domain.model.VulnerabilityAlert
import kotlinx.coroutines.flow.Flow

interface AlertRepository {
    fun getAlerts(): Flow<List<VulnerabilityAlert>>
    suspend fun syncAlerts()

    fun getUserTechnologies(): Flow<List<UserTechnology>>
    suspend fun addUserTechnology(name: String, version: String)
    suspend fun deleteUserTechnology(id: Long)
    suspend fun checkCvesForTechnology(name: String, version: String): List<VulnerabilityAlert>
    suspend fun updateAlertResolution(id: String, isResolved: Boolean)
}

