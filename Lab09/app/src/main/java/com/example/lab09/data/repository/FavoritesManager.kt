package com.example.lab09.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FavoritesManager {
    private lateinit var prefs: SharedPreferences
    private val _favoritesFlow = MutableStateFlow<Set<String>>(emptySet())
    val favoritesFlow: StateFlow<Set<String>> = _favoritesFlow.asStateFlow()

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.applicationContext.getSharedPreferences("crypto_prefs", Context.MODE_PRIVATE)
            _favoritesFlow.value = getFavoritesFromPrefs()
        }
    }

    private fun getFavoritesFromPrefs(): Set<String> {
        return prefs.getStringSet("favorites", emptySet()) ?: emptySet()
    }

    fun isFavorite(coinId: String): Boolean {
        return _favoritesFlow.value.contains(coinId)
    }

    fun toggleFavorite(coinId: String): Boolean {
        val current = getFavoritesFromPrefs().toMutableSet()
        val isAdded = if (current.contains(coinId)) {
            current.remove(coinId)
            false
        } else {
            current.add(coinId)
            true
        }
        prefs.edit().putStringSet("favorites", current).apply()
        _favoritesFlow.value = current
        return isAdded
    }
}
