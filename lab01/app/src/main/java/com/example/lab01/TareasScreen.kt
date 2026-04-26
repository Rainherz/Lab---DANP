package com.example.lab01

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TareasScreen(viewModel: TareasViewModel) {
    val tareas by viewModel.tareas.collectAsState()
    val filtro by viewModel.filtro.collectAsState()

    var textoInput by remember { mutableStateOf("") }
    var tareaEditando by remember { mutableStateOf<Tarea?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Gestor de Tareas",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = textoInput,
            onValueChange = { textoInput = it },
            label = { Text(if (tareaEditando != null) "Editar tarea" else "Nueva tarea") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (textoInput.isNotBlank()) {
                    if (tareaEditando != null) {
                        viewModel.editarTarea(tareaEditando!!.id, textoInput)
                        tareaEditando = null
                    } else {
                        viewModel.agregarTarea(textoInput)
                    }
                    textoInput = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (tareaEditando != null) "Guardar cambios" else "Agregar tarea")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = filtro == FiltroTareas.TODAS,
                onClick = { viewModel.setFiltro(FiltroTareas.TODAS) },
                label = { Text("Todas") }
            )
            FilterChip(
                selected = filtro == FiltroTareas.PENDIENTES,
                onClick = { viewModel.setFiltro(FiltroTareas.PENDIENTES) },
                label = { Text("Pendientes") }
            )
            FilterChip(
                selected = filtro == FiltroTareas.COMPLETADAS,
                onClick = { viewModel.setFiltro(FiltroTareas.COMPLETADAS) },
                label = { Text("Completadas") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tareas) { tarea ->
                ItemTarea(
                    tarea = tarea,
                    onToggle = { viewModel.toggleCompletada(tarea.id) },
                    onEdit = {
                        tareaEditando = tarea
                        textoInput = tarea.titulo
                    },
                    onDelete = {
                        viewModel.eliminarTarea(tarea.id)
                        if (tareaEditando?.id == tarea.id) {
                            tareaEditando = null
                            textoInput = ""
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ItemTarea(
    tarea: Tarea,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (tarea.completada) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = tarea.completada,
                    onCheckedChange = { onToggle() }
                )
                Text(
                    text = tarea.titulo,
                    modifier = Modifier.padding(start = 8.dp),
                    color = if (tarea.completada) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface,
                    style = if (tarea.completada) MaterialTheme.typography.bodyLarge.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough) else MaterialTheme.typography.bodyLarge
                )
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
