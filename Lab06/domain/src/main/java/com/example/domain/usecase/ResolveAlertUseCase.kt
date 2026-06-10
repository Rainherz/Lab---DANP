package com.example.domain.usecase

import com.example.domain.repository.AlertRepository
import javax.inject.Inject

class ResolveAlertUseCase @Inject constructor(
    private val repository: AlertRepository
) {
    suspend operator fun invoke(id: String, isResolved: Boolean) {
        repository.updateAlertResolution(id, isResolved)
    }
}
