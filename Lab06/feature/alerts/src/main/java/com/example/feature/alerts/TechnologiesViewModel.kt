package com.example.feature.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.UserTechnology
import com.example.domain.model.VulnerabilityAlert

import com.example.domain.usecase.AddUserTechnologyUseCase
import com.example.domain.usecase.CheckTechnologyCvesUseCase
import com.example.domain.usecase.DeleteUserTechnologyUseCase
import com.example.domain.usecase.GetAlertsUseCase
import com.example.domain.usecase.GetUserTechnologiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TechnologiesViewModel @Inject constructor(
    private val getUserTechnologiesUseCase: GetUserTechnologiesUseCase,
    private val getAlertsUseCase: GetAlertsUseCase,
    private val addUserTechnologyUseCase: AddUserTechnologyUseCase,
    private val deleteUserTechnologyUseCase: DeleteUserTechnologyUseCase,
    private val checkTechnologyCvesUseCase: CheckTechnologyCvesUseCase
) : ViewModel() {

    private val _isSearching = MutableStateFlow(false)
    private val _searchResults = MutableStateFlow<List<VulnerabilityAlert>?>(null)
    private val _lastSearchQuery = MutableStateFlow<String?>(null)
    private val _error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<TechnologiesUiState> = combine(
        getUserTechnologiesUseCase(),
        getAlertsUseCase(),
        _isSearching,
        _searchResults,
        _lastSearchQuery,
        _error
    ) { flows ->
        @Suppress("UNCHECKED_CAST")
        TechnologiesUiState(
            userTechnologies = flows[0] as List<UserTechnology>,
            alerts = flows[1] as List<VulnerabilityAlert>,
            isSearching = flows[2] as Boolean,
            searchResults = flows[3] as List<VulnerabilityAlert>?,
            lastSearchQuery = flows[4] as String?,
            error = flows[5] as String?
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TechnologiesUiState()
    )

    fun checkCves(name: String, version: String) {
        if (name.isBlank() || version.isBlank()) {
            _error.value = "Por favor ingresa nombre y versión."
            return
        }
        viewModelScope.launch {
            _isSearching.value = true
            _error.value = null
            _lastSearchQuery.value = "$name v$version"
            try {
                val results = checkTechnologyCvesUseCase(name, version)
                _searchResults.value = results
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error al buscar vulnerabilidades."
                _searchResults.value = emptyList()
            } finally {
                _isSearching.value = false
            }
        }
    }

    fun addTechnology(name: String, version: String) {
        if (name.isBlank() || version.isBlank()) {
            _error.value = "El nombre y versión no pueden estar vacíos."
            return
        }
        viewModelScope.launch {
            _error.value = null
            try {
                addUserTechnologyUseCase(name, version)
                clearSearchResults()
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error al agregar la tecnología."
            }
        }
    }

    fun deleteTechnology(id: Long) {
        viewModelScope.launch {
            try {
                deleteUserTechnologyUseCase(id)
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error al eliminar la tecnología."
            }
        }
    }

    fun clearSearchResults() {
        _searchResults.value = null
        _lastSearchQuery.value = null
    }

    fun dismissError() {
        _error.value = null
    }
}
