package com.example.domain.usecase

import com.example.domain.model.VulnerabilityAlert
import com.example.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FilterAlertsBySeverityUseCase @Inject constructor(
    private val repository: AlertRepository
) {
    operator fun invoke(severity: String): Flow<List<VulnerabilityAlert>> {
        return repository.getAlerts().map { alerts ->
            if (severity.isBlank() || severity == "Todos") {
                alerts
            } else {
                alerts.filter { it.severity.equals(severity, ignoreCase = true) }
            }
        }
    }
}
