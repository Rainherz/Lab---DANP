package com.example.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF6200EE),
                    secondary = Color(0xFF03DAC5),
                    background = Color(0xFFF5F5F5)
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun HabitItem(
    habit: HabitUiModel,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    // Animate color change when checking/unchecking
    val backgroundColor by animateColorAsState(
        targetValue = if (habit.isCompletedToday) Color(0xFFE8F5E9) else Color.White,
        animationSpec = tween(durationMillis = 300),
        label = "backgroundColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = habit.isCompletedToday,
                    onCheckedChange = onToggle,
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = habit.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = if (habit.isCompletedToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (habit.isCompletedToday) Color(0xFF2E7D32) else Color.Black
                        )
                    )
                    if (habit.currentStreak > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Streak",
                                tint = Color(0xFFFF9800),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${habit.currentStreak} días",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFE53935)
                )
            }
        }
    }
}

@Composable
fun HabitFilterChips(
    currentFilter: HabitFilter,
    onFilterSelected: (HabitFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FilterChip(
            selected = currentFilter == HabitFilter.ALL,
            onClick = { onFilterSelected(HabitFilter.ALL) },
            label = { Text("Todos") }
        )
        FilterChip(
            selected = currentFilter == HabitFilter.PENDING,
            onClick = { onFilterSelected(HabitFilter.PENDING) },
            label = { Text("Pendientes") }
        )
        FilterChip(
            selected = currentFilter == HabitFilter.COMPLETED,
            onClick = { onFilterSelected(HabitFilter.COMPLETED) },
            label = { Text("Completados") }
        )
    }
}

@Composable
fun HomeScreen(
    username: String,
    viewModel: HabitViewModel,
    onHabitClick: (Int) -> Unit,
    onLogout: () -> Unit
) {
    var input by remember { mutableStateOf("") }
    val habits by viewModel.habits.collectAsState()
    val filter by viewModel.filter.collectAsState()

    val completedCount = habits.count { it.isCompletedToday }
    val progress = if (habits.isNotEmpty()) {
        completedCount.toFloat() / habits.size
    } else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with Username and Logout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hola, $username \uD83D\uDC4B",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
                Text(
                    text = "Tus Hábitos",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Progress Bar
        if (filter == HabitFilter.ALL) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Progreso de hoy", style = MaterialTheme.typography.bodyMedium)
                    Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF4CAF50),
                    trackColor = Color(0xFFE0E0E0)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Filter Chips
        HabitFilterChips(currentFilter = filter, onFilterSelected = { viewModel.setFilter(it) })
        Spacer(modifier = Modifier.height(8.dp))

        // Input
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Nuevo hábito") },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (input.isNotBlank()) {
                        viewModel.addHabit(input)
                        input = ""
                    }
                },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text("Agregar")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // List
        if (habits.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No hay hábitos aquí.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn {
                items(habits, key = { it.id }) { habit ->
                    HabitItem(
                        habit = habit,
                        onToggle = { checked ->
                            viewModel.toggleHabitCompletion(habit.id, checked)
                        },
                        onDelete = {
                            viewModel.deleteHabit(habit.id)
                        },
                        onClick = {
                            onHabitClick(habit.id)
                        }
                    )
                }
            }
        }
    }
}
