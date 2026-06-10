package com.example.lab05.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab05.ui.components.CartItemRow
import com.example.lab05.ui.components.EmptyState
import com.example.lab05.ui.viewmodel.StoreViewModel
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
    viewModel: StoreViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val subtotal by viewModel.cartSubtotal.collectAsState()
    val total by viewModel.cartTotal.collectAsState()
    
    val scope = rememberCoroutineScope()
    val snackbarHostState = androidx.compose.runtime.remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 16.dp,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(16.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Subtotal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("$${String.format("%.2f", subtotal)}", fontWeight = FontWeight.Medium)
                                }
                                
                                val shippingFee = if (subtotal > 200.0 || subtotal == 0.0) 0.0 else 15.0
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Envío", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        text = if (shippingFee == 0.0) "Gratis" else "$${String.format("%.2f", shippingFee)}",
                                        color = if (shippingFee == 0.0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                
                                if (subtotal < 200.0 && subtotal > 0.0) {
                                    Text(
                                        text = "¡Agrega $${String.format("%.2f", 200.0 - subtotal)} más para envío GRATIS!",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }

                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Total General",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "$${String.format("%.2f", total)}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "¡Pedido procesado con éxito! Gracias por su compra.",
                                        actionLabel = "Aceptar"
                                    )
                                    viewModel.clearCart()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Proceder al Pago",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (cartItems.isEmpty()) {
                EmptyState(
                    title = "Tu carrito está vacío",
                    description = "Agrega algunos productos tecnológicos para comenzar tus compras.",
                    icon = Icons.Default.ShoppingCart
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(cartItems, key = { it.product.id }) { item ->
                        CartItemRow(
                            cartItem = item,
                            onIncrease = { viewModel.updateCartQuantity(item.product.id, item.quantity + 1) },
                            onDecrease = { viewModel.updateCartQuantity(item.product.id, item.quantity - 1) },
                            onRemove = { viewModel.removeFromCart(item.product.id) }
                        )
                    }
                }
            }
        }
    }
}
