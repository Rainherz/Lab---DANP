package com.example.lab01

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tareas_prefs")

class TaskRepository(private val context: Context) {
    private val TAREAS_KEY = stringPreferencesKey("lista_tareas")

    val tareasFlow: Flow<List<Tarea>> = context.dataStore.data.map { preferences ->
        val json = preferences[TAREAS_KEY] ?: "[]"
        try {
            Json.decodeFromString<List<Tarea>>(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveTareas(tareas: List<Tarea>) {
        context.dataStore.edit { preferences ->
            val json = Json.encodeToString(tareas)
            preferences[TAREAS_KEY] = json
        }
    }
}



