package com.example.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.VulnerabilityStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val vulnerabilityStatsUseCase: VulnerabilityStatsUseCase
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = vulnerabilityStatsUseCase()
        .map { stats ->
            DashboardUiState(
                isLoading = false,
                severityCounts = stats.severityCounts,
                technologyCounts = stats.technologyCounts,
                userTechnologyCount = stats.userTechnologyCount,
                errorMessage = null
            )

        }
        .catch { error ->
            emit(DashboardUiState(errorMessage = error.localizedMessage ?: "Error loading dashboard statistics"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUiState(isLoading = true)
        )
}
