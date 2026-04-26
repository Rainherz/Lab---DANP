package com.example.lab01

import kotlinx.serialization.Serializable

@Serializable
data class Tarea(
    val id: Int,
    val titulo: String,
    val completada: Boolean = false
)

@Serializable
data class ListaTareas(
    val tareas: List<Tarea>
)