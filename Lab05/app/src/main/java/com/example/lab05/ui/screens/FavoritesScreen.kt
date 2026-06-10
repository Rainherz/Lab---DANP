package com.example.lab05.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab05.ui.components.EmptyState
import com.example.lab05.ui.components.ProductCard
import com.example.lab05.ui.viewmodel.StoreViewModel

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: StoreViewModel,
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.favorites.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (favorites.isEmpty()) {
            EmptyState(
                title = "Aún no tienes favoritos",
                description = "Explora los productos de la tienda y presiona el ícono de corazón para guardarlos aquí.",
                icon = Icons.Default.FavoriteBorder
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(favorites) { product ->
                    ProductCard(
                        product = product,
                        isFavorite = true,
                        onToggleFavorite = { viewModel.toggleFavorite(product.id) },
                        onAddToCart = { viewModel.addToCart(product.id) },
                        onViewDetail = {
                            navController.navigate("detail/${product.id}")
                        }
                    )
                }
            }
        }
    }
}
