package com.example.lab07.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab07.domain.model.Category
import com.example.lab07.domain.model.Product
import com.example.lab07.ui.viewmodel.ProductCatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: ProductCatalogViewModel,
    onAdjustStockClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val products by viewModel.products.collectAsState()
    val categories by viewModel.categories.collectAsState()

    val totalProducts = products.size
    val totalCategories = categories.size
    val inventoryValue = remember(products) {
        products.sumOf { it.price * it.stock }
    }
    
    val lowStockProducts = remember(products) {
        products.filter { it.stock < 5 }
    }
    val lowStockCount = lowStockProducts.size

    val categoryDistribution = remember(products, categories) {
        categories.map { category ->
            val count = products.count { it.categoryId == category.id }
            val proportion = if (totalProducts > 0) count.toFloat() / totalProducts else 0f
            Triple(category.name, count, proportion)
        }.sortedByDescending { it.second }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Métricas",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // KPI Panel (Minimalist Grid)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "PRODUCTOS",
                        value = "$totalProducts",
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "CATEGORÍAS",
                        value = "$totalCategories",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "VALOR INVENTARIO",
                        value = String.format("$%.2f", inventoryValue),
                        modifier = Modifier.weight(1.3f)
                    )
                    KpiCard(
                        title = "ALERTAS STOCK",
                        value = "$lowStockCount",
                        isAlert = lowStockCount > 0,
                        modifier = Modifier.weight(0.9f)
                    )
                }
            }

            // Category Distribution Chart (Minimal design, flat bars)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Distribución de Inventario",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    if (categoryDistribution.isEmpty()) {
                        Text(
                            "Sin datos de categorías",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        categoryDistribution.forEach { (name, count, proportion) ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "$count ${if (count == 1) "ud" else "uds"}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                
                                // Progress bar using solid primary container accent
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(proportion)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(MaterialTheme.colorScheme.secondary)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Low Stock Warnings List
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 90.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Alertas Críticas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "Stock inferior a 5 unidades.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (lowStockProducts.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "✔ Todo el stock está al día",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            lowStockProducts.forEach { product ->
                                LowStockRow(
                                    product = product,
                                    onClick = { onAdjustStockClick(product) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    isTransit: Boolean = false,
    isAlert: Boolean = false
) {
    val containerColor = when {
        isAlert -> Color(0xFFFEE2E2)
        else -> MaterialTheme.colorScheme.surface
    }
    
    val outlineColor = when {
        isAlert -> Color(0xFFFCA5A5)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    }

    val valueColor = when {
        isAlert -> Color(0xFF991B1B)
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, outlineColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.height(90.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (isAlert) Color(0xFF991B1B).copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = valueColor,
                maxLines = 1
            )
        }
    }
}

@Composable
fun LowStockRow(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isOutOfStock = product.stock == 0
    val cardColor = if (isOutOfStock) Color(0xFFFEF2F2) else Color(0xFFFFFBEB)
    val strokeColor = if (isOutOfStock) Color(0xFFFEE2E2) else Color(0xFFFEF3C7)
    val alertColor = if (isOutOfStock) Color(0xFFEF4444) else Color(0xFFF59E0B)

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = BorderStroke(1.dp, strokeColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Text(product.imageUri.ifEmpty { "📦" }, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isOutOfStock) "Sin stock disponible" else "Quedan solo ${product.stock} uds",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isOutOfStock) Color(0xFFB91C1C) else Color(0xFFB45309)
                )
            }

            Icon(
                Icons.Default.Warning,
                contentDescription = "Alerta",
                tint = alertColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
