package com.example.lab09.ui.detail

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.lab09.util.NumberFormatter
import com.example.lab09.util.UiState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
    coinId: String,
    onBack: () -> Unit
) {
    val viewModel: CoinDetailViewModel = viewModel(
        key = coinId,
        factory = CoinDetailViewModelFactory(coinId)
    )
    val detailState by viewModel.detailState.collectAsStateWithLifecycle()
    val chartState by viewModel.chartState.collectAsStateWithLifecycle()
    val selectedDays by viewModel.selectedDays.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Favorito",
                            tint = if (isFavorite) Color(0xFFFBC02D) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        when (val state = detailState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "Error al cargar información",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = state.message, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = { viewModel.loadData() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            is UiState.Success -> {
                val coin = state.data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = coin.image?.large,
                            contentDescription = coin.name,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = coin.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = coin.id.uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Price Section
                    val marketData = coin.marketData
                    if (marketData != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = NumberFormatter.formatPrice(marketData.currentPrice?.get("usd") ?: 0.0),
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 36.sp
                                )
                            }
                            val change = marketData.priceChangePercentage24h ?: 0.0
                            val trendColor = if (change >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                            val pillBgColor = trendColor.copy(alpha = 0.12f)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(pillBgColor)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = NumberFormatter.formatPercentage(change),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = trendColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Interactive Chart Section
                        ChartSection(
                            chartState = chartState,
                            selectedDays = selectedDays,
                            onDaysSelected = { viewModel.updateSelectedDays(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Interactive Calculator Card
                        CurrencyConverterCard(
                            currentPrice = marketData.currentPrice?.get("usd") ?: 0.0,
                            coinSymbol = coin.id
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Detailed Stats Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Estadísticas del Mercado",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                StatRow("Cap. de Mercado", NumberFormatter.formatMarketCap(marketData.marketCap?.get("usd") ?: 0.0))
                                StatRow("Volumen (24h)", NumberFormatter.formatVolume(marketData.marketCap?.get("usd") ?: 0.0))
                                StatRow("Máximo Histórico (ATH)", NumberFormatter.formatPrice(marketData.ath?.get("usd") ?: 0.0))
                                StatRow("Suministro Circulante", formatSupply(marketData.circulatingSupply))
                                if (marketData.totalSupply != null) {
                                    StatRow("Suministro Total", formatSupply(marketData.totalSupply))
                                }
                                if (marketData.maxSupply != null) {
                                    StatRow("Suministro Máximo", formatSupply(marketData.maxSupply))
                                }

                                // Interactive Link
                                val uriHandler = LocalUriHandler.current
                                val homepage = coin.links?.homepage?.firstOrNull { !it.isNullOrBlank() }
                                if (!homepage.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = { uriHandler.openUri(homepage) },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Visitar Web Oficial", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Description Card
                        val description = coin.description?.en
                        if (!description.isNullOrBlank()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Acerca de ${coin.name}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = stripHtml(description),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ChartSection(
    chartState: UiState<List<List<Double>>>,
    selectedDays: Int,
    onDaysSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Historial de Precios",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Timeframe selector
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    val daysOptions = listOf(1, 7, 30, 90)
                    val labelMap = mapOf(1 to "1D", 7 to "7D", 30 to "30D", 90 to "90D")

                    daysOptions.forEach { days ->
                        FilterChip(
                            selected = selectedDays == days,
                            onClick = { onDaysSelected(days) },
                            label = { Text(labelMap[days] ?: "${days}D", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            border = null,
                            modifier = Modifier.height(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = chartState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is UiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Historial no disponible temporalmente",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                is UiState.Success -> {
                    val prices = state.data
                    if (prices.isNotEmpty()) {
                        val firstPrice = prices.first().getOrNull(1) ?: 0.0
                        val lastPrice = prices.last().getOrNull(1) ?: 0.0
                        val isPositive = lastPrice >= firstPrice
                        val lineColor = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)

                        Column(modifier = Modifier.weight(1f)) {
                            // Max/Min indicators
                            val minPrice = prices.minOf { it.getOrNull(1) ?: 0.0 }
                            val maxPrice = prices.maxOf { it.getOrNull(1) ?: 0.0 }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Mín: ${NumberFormatter.formatPrice(minPrice)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Máx: ${NumberFormatter.formatPrice(maxPrice)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                val range = (maxPrice - minPrice).coerceAtLeast(0.000001)
                                val stepX = size.width / (prices.size - 1).coerceAtLeast(1)

                                // Draw dotted reference grid lines (e.g. 3 lines)
                                val gridColor = lineColor.copy(alpha = 0.1f)
                                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                                for (i in 1..3) {
                                    val yGrid = size.height * (i * 0.25f)
                                    drawLine(
                                        color = gridColor,
                                        start = Offset(0f, yGrid),
                                        end = Offset(size.width, yGrid),
                                        strokeWidth = 1.dp.toPx(),
                                        pathEffect = dashEffect
                                    )
                                }

                                // Create line path
                                val path = Path()
                                val fillPath = Path()

                                prices.forEachIndexed { index, point ->
                                    val x = index * stepX
                                    val yPrice = point.getOrNull(1) ?: 0.0
                                    val y = size.height - ((yPrice - minPrice) / range * size.height).toFloat()

                                    if (index == 0) {
                                        path.moveTo(x, y)
                                        fillPath.moveTo(x, y)
                                    } else {
                                        path.lineTo(x, y)
                                        fillPath.lineTo(x, y)
                                    }
                                }

                                // Close the fill path to draw the gradient under the line
                                fillPath.lineTo(size.width, size.height)
                                fillPath.lineTo(0f, size.height)
                                fillPath.close()

                                // Draw gradient fill
                                drawPath(
                                    path = fillPath,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(lineColor.copy(alpha = 0.25f), Color.Transparent)
                                    )
                                )

                                // Draw curve line
                                drawPath(
                                    path = path,
                                    color = lineColor,
                                    style = Stroke(width = 2.5.dp.toPx())
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay suficientes datos disponibles")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencyConverterCard(currentPrice: Double, coinSymbol: String) {
    var usdValue by remember { mutableStateOf("") }
    var cryptoValue by remember { mutableStateOf("") }
    var isUpdatingUsd by remember { mutableStateOf(false) }
    var isUpdatingCrypto by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Conversor de Moneda",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // USD Input
                OutlinedTextField(
                    value = usdValue,
                    onValueChange = { input ->
                        if (!isUpdatingCrypto) {
                            isUpdatingUsd = true
                            usdValue = input
                            val parsed = input.toDoubleOrNull()
                            if (parsed != null && currentPrice > 0) {
                                cryptoValue = String.format(Locale.US, "%.6f", parsed / currentPrice)
                            } else {
                                cryptoValue = ""
                            }
                            isUpdatingUsd = false
                        }
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("USD", fontSize = 12.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )

                Text(
                    text = "⇌",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                // Crypto Input
                OutlinedTextField(
                    value = cryptoValue,
                    onValueChange = { input ->
                        if (!isUpdatingUsd) {
                            isUpdatingCrypto = true
                            cryptoValue = input
                            val parsed = input.toDoubleOrNull()
                            if (parsed != null) {
                                usdValue = String.format(Locale.US, "%.2f", parsed * currentPrice)
                            } else {
                                usdValue = ""
                            }
                            isUpdatingCrypto = false
                        }
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text(coinSymbol.uppercase(), fontSize = 12.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }
        }
    }
}

private fun formatSupply(supply: Double?): String {
    if (supply == null) return "N/A"
    return when {
        supply >= 1_000_000_000 -> String.format(Locale.US, "%.2fB", supply / 1_000_000_000)
        supply >= 1_000_000 -> String.format(Locale.US, "%.2fM", supply / 1_000_000)
        supply >= 1_000 -> String.format(Locale.US, "%.2fK", supply / 1_000)
        else -> String.format(Locale.US, "%.2f", supply)
    }
}

private fun stripHtml(html: String): String {
    return html.replace(Regex("<[^>]*>"), "")
}
