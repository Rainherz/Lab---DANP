package com.example.data.repository

import com.example.core.database.dao.UserTechnologyDao
import com.example.core.database.dao.VulnerabilityDao
import com.example.core.database.entity.UserTechnologyEntity
import com.example.core.database.entity.VulnerabilityEntity
import com.example.core.network.api.CveApiService
import com.example.domain.model.UserTechnology
import com.example.domain.model.VulnerabilityAlert
import com.example.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlertRepositoryImpl @Inject constructor(
    private val cveApiService: CveApiService,
    private val vulnerabilityDao: VulnerabilityDao,
    private val userTechnologyDao: UserTechnologyDao
) : AlertRepository {

    override fun getAlerts(): Flow<List<VulnerabilityAlert>> {
        return vulnerabilityDao.getAlertsFlow().map { entities ->
            entities.map { entity ->
                VulnerabilityAlert(
                    id = entity.id,
                    cve = entity.cve,
                    severity = entity.severity,
                    description = entity.description,
                    date = entity.date,
                    affectedTechnology = entity.affectedTechnology,
                    referenceUrl = entity.referenceUrl,
                    isResolved = entity.isResolved
                )
            }
        }
    }

    override suspend fun syncAlerts() {
        val techs = userTechnologyDao.getUserTechnologiesFlow().first()
        if (techs.isEmpty()) {
            vulnerabilityDao.clearAlerts()
            return
        }

        val allAlerts = mutableListOf<VulnerabilityEntity>()
        techs.forEach { tech ->
            val alerts = fetchCvesFromNetwork(tech.name, tech.version, tech.id)
            allAlerts.addAll(alerts)
        }

        vulnerabilityDao.clearAndInsertAlerts(allAlerts)
    }

    override fun getUserTechnologies(): Flow<List<UserTechnology>> {
        return userTechnologyDao.getUserTechnologiesFlow().map { entities ->
            entities.map { entity ->
                UserTechnology(
                    id = entity.id,
                    name = entity.name,
                    version = entity.version
                )
            }
        }
    }

    override suspend fun addUserTechnology(name: String, version: String) {
        val techEntity = UserTechnologyEntity(name = name, version = version)
        val techId = userTechnologyDao.insertUserTechnology(techEntity)
        
        val alerts = fetchCvesFromNetwork(name, version, techId)
        vulnerabilityDao.insertAlerts(alerts)
    }

    override suspend fun deleteUserTechnology(id: Long) {
        userTechnologyDao.deleteUserTechnology(id)
        vulnerabilityDao.deleteAlertsByTechnologyId(id)
    }

    override suspend fun checkCvesForTechnology(name: String, version: String): List<VulnerabilityAlert> {
        return fetchCvesFromNetwork(name, version, null).map { entity ->
            VulnerabilityAlert(
                id = entity.id,
                cve = entity.cve,
                severity = entity.severity,
                description = entity.description,
                date = entity.date,
                affectedTechnology = entity.affectedTechnology,
                referenceUrl = entity.referenceUrl,
                isResolved = entity.isResolved
            )
        }
    }

    override suspend fun updateAlertResolution(id: String, isResolved: Boolean) {
        vulnerabilityDao.updateResolution(id, isResolved)
    }

    private suspend fun fetchCvesFromNetwork(
        name: String,
        version: String,
        techId: Long?
    ): List<VulnerabilityEntity> {
        val query = "$name $version"
        val response = try {
            cveApiService.getCves(keyword = query)
        } catch (e: Exception) {
            null
        } ?: return emptyList()

        return response.vulnerabilities?.mapNotNull { container ->
            val cve = container.cve ?: return@mapNotNull null
            val id = cve.id ?: return@mapNotNull null

            val description = cve.descriptions?.firstOrNull { it.lang == "es" }?.value
                ?: cve.descriptions?.firstOrNull { it.lang == "en" }?.value
                ?: "No description available"

            val rawSeverity = cve.metrics?.cvssMetricV31?.firstOrNull()?.cvssData?.baseSeverity ?: "LOW"
            val severity = when (rawSeverity.uppercase()) {
                "CRITICAL" -> "CRÍTICO"
                "HIGH" -> "ALTO"
                "MEDIUM" -> "MEDIO"
                "LOW" -> "BAJO"
                else -> "BAJO"
            }

            val date = cve.published?.take(10) ?: "Unknown Date"

            // Obtener el enlace de la solución (priorizar parches en los tags)
            val refUrl = cve.references?.firstOrNull { ref -> 
                ref.tags?.any { tag -> tag.lowercase().contains("patch") } == true 
            }?.url ?: cve.references?.firstOrNull()?.url

            VulnerabilityEntity(
                id = id,
                cve = id,
                severity = severity,
                description = description,
                date = date,
                affectedTechnology = "$name $version",
                userTechnologyId = techId,
                referenceUrl = refUrl,
                isResolved = false // Por defecto inicia sin solucionar
            )
        } ?: emptyList()
    }
}
