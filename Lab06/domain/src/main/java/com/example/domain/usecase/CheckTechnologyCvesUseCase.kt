package com.example.domain.usecase

import com.example.domain.model.VulnerabilityAlert
import com.example.domain.repository.AlertRepository
import javax.inject.Inject

class CheckTechnologyCvesUseCase @Inject constructor(
    private val repository: AlertRepository
) {
    suspend operator fun invoke(name: String, version: String): List<VulnerabilityAlert> {
        return repository.checkCvesForTechnology(name, version)
    }
}
