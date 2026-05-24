package com.example.lab05.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab05.ui.components.CategoryCard
import com.example.lab05.ui.components.EmptyState
import com.example.lab05.ui.components.ProductCard
import com.example.lab05.ui.components.SearchBar
import com.example.lab05.ui.viewmodel.StoreViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: StoreViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val products by viewModel.products.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    val favoriteIds = remember(favorites) { favorites.map { it.id }.toSet() }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.setSearchQuery(it) }
        )

        // Categories Section Header
        Text(
            text = "Categorías",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(130.dp)
        ) {
            // "Todos" Category Card
            item {
                CategoryCard(
                    name = "Todos",
                    productCount = null,
                    isSelected = selectedCategory == null,
                    onClick = { viewModel.selectCategory(null) },
                    modifier = Modifier.width(110.dp).fillMaxHeight()
                )
            }

            // Real dynamic categories
            items(viewModel.categories) { (categoryName, count) ->
                CategoryCard(
                    name = categoryName,
                    productCount = count,
                    isSelected = selectedCategory == categoryName,
                    onClick = { viewModel.selectCategory(categoryName) },
                    modifier = Modifier.width(110.dp).fillMaxHeight()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Productos disponibles",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        if (products.isEmpty()) {
            EmptyState(
                title = "No se encontraron productos",
                description = "Intenta buscando otro término o quitando los filtros de categoría.",
                icon = Icons.Default.SearchOff
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        isFavorite = favoriteIds.contains(product.id),
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