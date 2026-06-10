package com.example.lab01

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FiltroTareas {
    TODAS, PENDIENTES, COMPLETADAS
}

class TareasViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _filtro = MutableStateFlow(FiltroTareas.TODAS)
    val filtro: StateFlow<FiltroTareas> = _filtro

    val tareas: StateFlow<List<Tarea>> = repository.tareasFlow
        .combine(_filtro) { list, filtro ->
            when (filtro) {
                FiltroTareas.TODAS -> list
                FiltroTareas.PENDIENTES -> list.filter { !it.completada }
                FiltroTareas.COMPLETADAS -> list.filter { it.completada }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun agregarTarea(titulo: String) {
        viewModelScope.launch {
            val currentList = repository.tareasFlow.first()
            val newId = (currentList.maxOfOrNull { it.id } ?: 0) + 1
            val nuevaTarea = Tarea(id = newId, titulo = titulo)
            repository.saveTareas(currentList + nuevaTarea)
        }
    }

    fun editarTarea(id: Int, nuevoTitulo: String) {
        viewModelScope.launch {
            val currentList = repository.tareasFlow.first()
            val newList = currentList.map {
                if (it.id == id) it.copy(titulo = nuevoTitulo) else it
            }
            repository.saveTareas(newList)
        }
    }

    fun toggleCompletada(id: Int) {
        viewModelScope.launch {
            val currentList = repository.tareasFlow.first()
            val newList = currentList.map {
                if (it.id == id) it.copy(completada = !it.completada) else it
            }
            repository.saveTareas(newList)
        }
    }

    fun eliminarTarea(id: Int) {
        viewModelScope.launch {
            val currentList = repository.tareasFlow.first()
            val newList = currentList.filter { it.id != id }
            repository.saveTareas(newList)
        }
    }

    fun setFiltro(nuevoFiltro: FiltroTareas) {
        _filtro.value = nuevoFiltro
    }
}
