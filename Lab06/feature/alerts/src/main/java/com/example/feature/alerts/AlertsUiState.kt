package com.example.feature.alerts

import com.example.domain.model.VulnerabilityAlert

data class AlertsUiState(
    val isLoading: Boolean = false,
    val alerts: List<VulnerabilityAlert> = emptyList(),
    val selectedSeverity: String = "Todos",
    val errorMessage: String? = null
)
