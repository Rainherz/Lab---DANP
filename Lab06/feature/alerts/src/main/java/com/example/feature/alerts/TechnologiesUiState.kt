package com.example.feature.alerts

import com.example.domain.model.UserTechnology
import com.example.domain.model.VulnerabilityAlert

data class TechnologiesUiState(
    val userTechnologies: List<UserTechnology> = emptyList(),
    val alerts: List<VulnerabilityAlert> = emptyList(),
    val isSearching: Boolean = false,
    val searchResults: List<VulnerabilityAlert>? = null,
    val lastSearchQuery: String? = null,
    val error: String? = null
)

