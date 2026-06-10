package com.example.feature.dashboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.SeverityCritical
import com.example.core.ui.SeverityHigh
import com.example.core.ui.SeverityLow
import com.example.core.ui.SeverityMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Resumen de Seguridad (Dashboard)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (state.errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            val totalAlerts = state.severityCounts.values.sum()
            
            // Calculate security score
            val criticalCount = state.severityCounts["CRÍTICO"] ?: 0
            val highCount = state.severityCounts["ALTO"] ?: 0
            val mediumCount = state.severityCounts["MEDIO"] ?: 0
            val lowCount = state.severityCounts["BAJO"] ?: 0
            
            val scoreDeductions = (criticalCount * 20) + (highCount * 12) + (mediumCount * 6) + (lowCount * 2)
            val securityScore = (100 - scoreDeductions).coerceIn(0, 100)
            
            val securityGrade = when (securityScore) {
                in 95..100 -> "A"
                in 80..94 -> "B"
                in 65..79 -> "C"
                in 50..64 -> "D"
                else -> "F"
            }
            
            val securityLabel = when (securityScore) {
                in 95..100 -> "Excelente - Stack Seguro"
                in 80..94 -> "Bueno - Stack Estable"
                in 65..79 -> "Aceptable - Requiere Revisión"
                in 50..64 -> "Riesgo Moderado"
                else -> "Inseguro - ¡Acción Requerida!"
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Security Score Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Puntaje de Seguridad Global",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (state.userTechnologyCount == 0) {
                            Text(
                                text = "Registra tus tecnologías en la pestaña 'Tecnologías' para calcular la puntuación de seguridad de tu infraestructura.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            SecurityScoreGauge(
                                score = securityScore,
                                grade = securityGrade,
                                label = securityLabel,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Alertas de Severidad",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (totalAlerts > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SeverityDonutChart(
                                    counts = state.severityCounts,
                                    total = totalAlerts,
                                    modifier = Modifier.size(160.dp)
                                )
                                Spacer(modifier = Modifier.width(24.dp))
                                SeverityLegend(counts = state.severityCounts)
                            }
                        } else {
                            Text(
                                "No hay datos disponibles",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Tecnologías Afectadas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (state.technologyCounts.isNotEmpty()) {
                            TechnologyBarChart(techCounts = state.technologyCounts)
                        } else {
                            Text(
                                "No hay datos disponibles",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SeverityDonutChart(
    counts: Map<String, Int>,
    total: Int,
    modifier: Modifier = Modifier
) {
    val progressAnim = remember { Animatable(0f) }
    LaunchedEffect(counts) {
        progressAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    val items = listOf(
        "CRÍTICO" to SeverityCritical,
        "ALTO" to SeverityHigh,
        "MEDIO" to SeverityMedium,
        "BAJO" to SeverityLow
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 36f
            val radius = (size.minDimension - strokeWidth) / 2
            val center = Offset(size.width / 2, size.height / 2)
            var startAngle = -90f

            items.forEach { (key, color) ->
                val count = counts[key] ?: 0
                if (count > 0) {
                    val sweepAngle = (count.toFloat() / total) * 360f * progressAnim.value
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    startAngle += sweepAngle
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$total",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Total CVEs",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SeverityLegend(counts: Map<String, Int>) {
    val items = listOf(
        "CRÍTICO" to SeverityCritical,
        "ALTO" to SeverityHigh,
        "MEDIO" to SeverityMedium,
        "BAJO" to SeverityLow
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach { (label, color) ->
            val count = counts[label] ?: 0
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(color, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$label: $count",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun TechnologyBarChart(
    techCounts: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    val maxCount = techCounts.values.maxOrNull() ?: 1

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        techCounts.forEach { (tech, count) ->
            val progressAnim = remember { Animatable(0f) }
            LaunchedEffect(count) {
                progressAnim.animateTo(
                    targetValue = count.toFloat() / maxCount,
                    animationSpec = tween(durationMillis = 800)
                )
            }

            val primaryColor = MaterialTheme.colorScheme.primary
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = tech,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$count",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                ) {
                    val barWidth = size.width * progressAnim.value
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.6f),
                                primaryColor
                            )
                        ),
                        size = Size(barWidth, size.height),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                    )
                }
            }
        }
    }
}

@Composable
fun SecurityScoreGauge(
    score: Int,
    grade: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val scoreAnim = remember { Animatable(0f) }
    LaunchedEffect(score) {
        scoreAnim.animateTo(
            targetValue = score.toFloat(),
            animationSpec = tween(durationMillis = 1200)
        )
    }

    val gradeColor = when (grade) {
        "A" -> Color(0xFF10B981) // Emerald
        "B" -> Color(0xFF06B6D4) // Cyan
        "C" -> Color(0xFFEAB308) // Yellow
        "D" -> Color(0xFFF97316) // Orange
        else -> Color(0xFFEF4444) // Red
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 14.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2
                val center = Offset(size.width / 2, size.height / 2)
                
                // Draw background track (240 degrees arc)
                drawArc(
                    color = Color.LightGray.copy(alpha = 0.2f),
                    startAngle = 150f,
                    sweepAngle = 240f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Draw active progress arc (240 degrees max)
                val sweepAngle = (scoreAnim.value / 100f) * 240f
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            gradeColor.copy(alpha = 0.6f),
                            gradeColor
                        ),
                        center = center
                    ),
                    startAngle = 150f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${scoreAnim.value.toInt()}",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Grado $grade",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = gradeColor
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = gradeColor,
            textAlign = TextAlign.Center
        )
    }
}
