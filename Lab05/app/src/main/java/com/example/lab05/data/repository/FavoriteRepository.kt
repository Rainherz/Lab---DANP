package com.example.lab05.data.repository

import android.content.Context
import android.content.SharedPreferences

class FavoriteRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("store_favorites_prefs", Context.MODE_PRIVATE)

    fun getFavoriteIds(): Set<Int> {
        val stringSet = prefs.getStringSet("favorite_ids", emptySet()) ?: emptySet()
        return stringSet.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun addFavorite(productId: Int) {
        val current = getFavoriteIds().map { it.toString() }.toMutableSet()
        current.add(productId.toString())
        prefs.edit().putStringSet("favorite_ids", current).apply()
    }

    fun removeFavorite(productId: Int) {
        val current = getFavoriteIds().map { it.toString() }.toMutableSet()
        current.remove(productId.toString())
        prefs.edit().putStringSet("favorite_ids", current).apply()
    }

    fun isFavorite(productId: Int): Boolean {
        return getFavoriteIds().contains(productId)
    }
}
