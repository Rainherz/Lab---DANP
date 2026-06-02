package com.example.feature.dashboard

data class DashboardUiState(
    val isLoading: Boolean = false,
    val severityCounts: Map<String, Int> = emptyMap(),
    val technologyCounts: Map<String, Int> = emptyMap(),
    val userTechnologyCount: Int = 0,
    val errorMessage: String? = null
)

