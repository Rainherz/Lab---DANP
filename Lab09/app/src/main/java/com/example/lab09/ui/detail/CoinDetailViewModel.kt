package com.example.lab09.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab09.data.model.CoinDetailResponse
import com.example.lab09.data.repository.CoinRepository
import com.example.lab09.data.repository.CoinRepositoryImpl
import com.example.lab09.data.repository.FavoritesManager
import com.example.lab09.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CoinDetailViewModel(
    private val coinId: String,
    private val repository: CoinRepository = CoinRepositoryImpl()
) : ViewModel() {

    private val _detailState = MutableStateFlow<UiState<CoinDetailResponse>>(UiState.Loading)
    val detailState: StateFlow<UiState<CoinDetailResponse>> = _detailState.asStateFlow()

    private val _chartState = MutableStateFlow<UiState<List<List<Double>>>>(UiState.Loading)
    val chartState: StateFlow<UiState<List<List<Double>>>> = _chartState.asStateFlow()

    val selectedDays = MutableStateFlow(7)

    val isFavorite: StateFlow<Boolean> = FavoritesManager.favoritesFlow
        .map { it.contains(coinId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoritesManager.isFavorite(coinId)
        )

    init {
        loadDetail()
        viewModelScope.launch {
            selectedDays.collect { days ->
                loadChart(days)
            }
        }
    }

    fun loadDetail() {
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            try {
                val detail = repository.getCoinDetail(coinId)
                _detailState.value = UiState.Success(detail)
            } catch (e: Exception) {
                _detailState.value = UiState.Error(
                    e.message ?: "Error al cargar detalle"
                )
            }
        }
    }

    fun loadChart(days: Int) {
        viewModelScope.launch {
            _chartState.value = UiState.Loading
            try {
                val chart = repository.getMarketChart(coinId, days = days)
                _chartState.value = UiState.Success(chart.prices)
            } catch (e: Exception) {
                _chartState.value = UiState.Error(
                    e.message ?: "Error al cargar gráfico"
                )
            }
        }
    }

    fun updateSelectedDays(days: Int) {
        selectedDays.value = days
    }

    fun toggleFavorite() {
        FavoritesManager.toggleFavorite(coinId)
    }

    fun loadData() {
        loadDetail()
        loadChart(selectedDays.value)
    }
}
