package com.example.lab09.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab09.data.model.CoinResponse
import com.example.lab09.data.repository.CoinRepository
import com.example.lab09.data.repository.CoinRepositoryImpl
import com.example.lab09.data.repository.FavoritesManager
import com.example.lab09.util.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class SortOption(val displayName: String) {
    MARKET_CAP_DESC("Capitalización"),
    PRICE_DESC("Precio: Mayor a Menor"),
    PRICE_ASC("Precio: Menor a Mayor"),
    CHANGE_24H_DESC("Ganadoras"),
    CHANGE_24H_ASC("Perdedoras")
}

data class FilterState(
    val query: String,
    val showOnlyFavorites: Boolean,
    val sortBy: SortOption,
    val favorites: Set<String>
)

class CoinListViewModel(
    private val repository: CoinRepository = CoinRepositoryImpl()
) : ViewModel() {

    private val _rawCoins = MutableStateFlow<List<CoinResponse>>(emptyList())
    private val _loading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    val searchQuery = MutableStateFlow("")
    val showOnlyFavorites = MutableStateFlow(false)
    val sortBy = MutableStateFlow(SortOption.MARKET_CAP_DESC)
    val lastUpdated = MutableStateFlow("")

    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private var autoRefreshJob: Job? = null

    private val filterStateFlow = combine(
        searchQuery,
        showOnlyFavorites,
        sortBy,
        FavoritesManager.favoritesFlow
    ) { query, onlyFavs, sortOpt, favSet ->
        FilterState(query, onlyFavs, sortOpt, favSet)
    }

    val uiState: StateFlow<UiState<List<CoinResponse>>> = combine(
        _rawCoins,
        _loading,
        _error,
        filterStateFlow
    ) { rawCoins, loading, error, filter ->
        when {
            error != null -> UiState.Error(error)
            loading && rawCoins.isEmpty() -> UiState.Loading
            else -> {
                var filtered = rawCoins.filter { coin ->
                    val matchesQuery = coin.name.contains(filter.query, ignoreCase = true) ||
                            coin.symbol.contains(filter.query, ignoreCase = true)
                    val matchesFav = !filter.showOnlyFavorites || filter.favorites.contains(coin.id)
                    matchesQuery && matchesFav
                }

                filtered = when (filter.sortBy) {
                    SortOption.MARKET_CAP_DESC -> filtered.sortedBy { it.marketCapRank ?: Int.MAX_VALUE }
                    SortOption.PRICE_DESC -> filtered.sortedByDescending { it.currentPrice ?: 0.0 }
                    SortOption.PRICE_ASC -> filtered.sortedBy { it.currentPrice ?: 0.0 }
                    SortOption.CHANGE_24H_DESC -> filtered.sortedByDescending { it.priceChangePercentage24h ?: -999.0 }
                    SortOption.CHANGE_24H_ASC -> filtered.sortedBy { it.priceChangePercentage24h ?: 999.0 }
                }

                UiState.Success(filtered)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        loadCoins()
        startAutoRefresh()
    }

    fun loadCoins() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val coins = repository.getCoins()
                _rawCoins.value = coins
                _error.value = null
                lastUpdated.value = dateFormat.format(Date())
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar las criptomonedas"
            } finally {
                _loading.value = false
            }
        }
    }

    private fun startAutoRefresh() {
        autoRefreshJob = viewModelScope.launch {
            while (isActive) {
                delay(60_000L)
                try {
                    val coins = repository.getCoins()
                    _rawCoins.value = coins
                    _error.value = null
                    lastUpdated.value = dateFormat.format(Date())
                } catch (_: Exception) {
                    // Silencio para no sobreescribir los datos previos en caso de error transitorio
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        autoRefreshJob?.cancel()
    }
}
