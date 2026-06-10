package com.example.feature.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.FilterAlertsBySeverityUseCase
import com.example.domain.usecase.ResolveAlertUseCase
import com.example.domain.usecase.SyncAlertsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val syncAlertsUseCase: SyncAlertsUseCase,
    private val filterAlertsBySeverityUseCase: FilterAlertsBySeverityUseCase,
    private val resolveAlertUseCase: ResolveAlertUseCase
) : ViewModel() {

    private val _selectedSeverity = MutableStateFlow("Todos")
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _alertsFlow = _selectedSeverity.flatMapLatest { severity ->
        filterAlertsBySeverityUseCase(severity)
    }

    val uiState: StateFlow<AlertsUiState> = combine(
        _alertsFlow,
        _selectedSeverity,
        _isLoading,
        _errorMessage
    ) { alerts, severity, isLoading, errorMessage ->
        AlertsUiState(
            isLoading = isLoading,
            alerts = alerts,
            selectedSeverity = severity,
            errorMessage = errorMessage
        )
    }.catch { error ->
        _errorMessage.value = error.message ?: "An unexpected error occurred"
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AlertsUiState(isLoading = true)
    )

    init {
        syncAlerts()
    }

    fun syncAlerts() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                syncAlertsUseCase()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to sync alerts: ${e.localizedMessage ?: "Unknown error"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectSeverity(severity: String) {
        _selectedSeverity.value = severity
    }

    fun toggleAlertResolution(id: String, isResolved: Boolean) {
        viewModelScope.launch {
            try {
                resolveAlertUseCase(id, isResolved)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update resolution: ${e.localizedMessage ?: "Unknown error"}"
            }
        }
    }

    fun dismissError() {
        _errorMessage.value = null
    }
}
