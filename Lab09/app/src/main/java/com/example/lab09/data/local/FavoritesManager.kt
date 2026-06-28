package com.example.lab09.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getFavorites(): Set<String> {
        val json = prefs.getString(KEY_FAVORITES, null) ?: return emptySet()
        val type = object : TypeToken<Set<String>>() {}.type
        val result: Set<String> = gson.fromJson(json, type) ?: emptySet()
        return result
    }

    fun toggleFavorite(coinId: String): Boolean {
        val current = getFavorites().toMutableSet()
        return if (current.contains(coinId)) {
            current.remove(coinId)
            saveFavorites(current)
            false
        } else {
            current.add(coinId)
            saveFavorites(current)
            true
        }
    }

    fun isFavorite(coinId: String): Boolean = getFavorites().contains(coinId)

    private fun saveFavorites(ids: Set<String>) {
        prefs.edit().putString(KEY_FAVORITES, gson.toJson(ids)).apply()
    }

    companion object {
        private const val PREFS_NAME = "crypto_favorites"
        private const val KEY_FAVORITES = "favorite_ids"
    }
}
