package com.example.domain.usecase

import com.example.domain.model.VulnerabilityAlert
import com.example.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlertsUseCase @Inject constructor(
    private val repository: AlertRepository
) {
    operator fun invoke(): Flow<List<VulnerabilityAlert>> = repository.getAlerts()
}
