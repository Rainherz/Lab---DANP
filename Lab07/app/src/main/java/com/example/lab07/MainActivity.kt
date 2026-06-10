package com.example.lab07

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab07.data.local.database.AppDatabase
import com.example.lab07.data.repository.ProductRepositoryImpl
import com.example.lab07.domain.model.Category
import com.example.lab07.domain.model.Product
import com.example.lab07.ui.screens.CategoryListScreen
import com.example.lab07.ui.screens.ProductAddEditScreen
import com.example.lab07.ui.screens.ProductListScreen
import com.example.lab07.ui.screens.CartScreen
import com.example.lab07.ui.screens.DashboardScreen
import com.example.lab07.ui.theme.Lab07Theme
import com.example.lab07.ui.viewmodel.ProductCatalogViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val db by lazy { AppDatabase.getDatabase(applicationContext) }
    private val repository by lazy { ProductRepositoryImpl(db.productDao(), db.categoryDao()) }
    private val viewModel: ProductCatalogViewModel by viewModels {
        ProductCatalogViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Populate database with mock data if it is empty
        lifecycleScope.launch {
            populateDatabaseIfEmpty()
        }

        setContent {
            Lab07Theme {
                MainAppContainer(viewModel)
            }
        }
    }

    private suspend fun populateDatabaseIfEmpty() {
        val currentCategories = repository.getAllCategories().first()
        if (currentCategories.isEmpty()) {
            // Insert mock categories
            val techId = repository.insertCategory(Category(name = "Tecnología"))
            val homeId = repository.insertCategory(Category(name = "Hogar"))
            val clothingId = repository.insertCategory(Category(name = "Ropa"))
            val foodId = repository.insertCategory(Category(name = "Alimentos"))

            // Insert mock products with various stock levels and favorites
            repository.insertProduct(Product(name = "iPhone 15", price = 999.99, description = "Último modelo de iPhone con pantalla Super Retina XDR.", imageUri = "📱", categoryId = techId, stock = 8, isFavorite = true))
            repository.insertProduct(Product(name = "Laptop Dell", price = 1200.00, description = "Procesador Intel i7, 16GB de RAM, 512GB SSD.", imageUri = "💻", categoryId = techId, stock = 3, isFavorite = false)) // Low stock
            repository.insertProduct(Product(name = "Audífonos Inalámbricos", price = 150.00, description = "Cancelación activa de ruido y batería de 30 horas.", imageUri = "🎧", categoryId = techId, stock = 15, isFavorite = true))
            
            repository.insertProduct(Product(name = "Sofá Confort", price = 450.00, description = "Sofá tapizado de tres cuerpos, muy cómodo para la sala.", imageUri = "📦", categoryId = homeId, stock = 2, isFavorite = false)) // Low stock
            repository.insertProduct(Product(name = "Cafetera Automática", price = 89.99, description = "Prepara café espresso y capuchino con solo tocar un botón.", imageUri = "☕", categoryId = homeId, stock = 0, isFavorite = false)) // Out of stock
            
            repository.insertProduct(Product(name = "Casaca Impermeable", price = 59.99, description = "Protección contra lluvia y viento, ideal para senderismo.", imageUri = "👕", categoryId = clothingId, stock = 20, isFavorite = false))
            repository.insertProduct(Product(name = "Zapatillas Deportivas", price = 79.99, description = "Suela amortiguada, perfectas para correr y entrenar.", imageUri = "👟", categoryId = clothingId, stock = 12, isFavorite = true))
            
            repository.insertProduct(Product(name = "Pizza Familiar", price = 14.99, description = "Pizza de pepperoni con queso mozzarella y salsa especial.", imageUri = "🍕", categoryId = foodId, stock = 5, isFavorite = false))
        }
    }
}

@Composable
fun MainAppContainer(viewModel: ProductCatalogViewModel) {
    val navController = rememberNavController()

    // Destinations
    val categoriesRoute = "categories"
    val productsRoute = "products"
    val cartRoute = "cart"
    val dashboardRoute = "dashboard"
    val addEditProductRoute = "product_add_edit/{productId}"

    // Track current route to control Bottom Bar visibility
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Show bottom navigation on all core routes
            if (currentRoute == categoriesRoute || currentRoute == productsRoute || 
                currentRoute == cartRoute || currentRoute == dashboardRoute) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Categorías") },
                        label = { Text("Categorías") },
                        selected = currentRoute == categoriesRoute,
                        onClick = {
                            if (currentRoute != categoriesRoute) {
                                navController.navigate(categoriesRoute) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.List, contentDescription = "Productos") },
                        label = { Text("Catálogo") },
                        selected = currentRoute == productsRoute,
                        onClick = {
                            if (currentRoute != productsRoute) {
                                navController.navigate(productsRoute) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito") },
                        label = { Text("Carrito") },
                        selected = currentRoute == cartRoute,
                        onClick = {
                            if (currentRoute != cartRoute) {
                                navController.navigate(cartRoute) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Star, contentDescription = "Estadísticas") },
                        label = { Text("Métricas") },
                        selected = currentRoute == dashboardRoute,
                        onClick = {
                            if (currentRoute != dashboardRoute) {
                                navController.navigate(dashboardRoute) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = categoriesRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(categoriesRoute) {
                CategoryListScreen(
                    viewModel = viewModel,
                    onCategoryClick = { category ->
                        viewModel.selectCategory(category.id)
                        navController.navigate(productsRoute) {
                            popUpTo(categoriesRoute) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(productsRoute) {
                ProductListScreen(
                    viewModel = viewModel,
                    onAddProductClick = {
                        navController.navigate("product_add_edit/0")
                    },
                    onEditProductClick = { product ->
                        navController.navigate("product_add_edit/${product.id}")
                    }
                )
            }

            composable(cartRoute) {
                CartScreen(viewModel = viewModel)
            }

            composable(dashboardRoute) {
                DashboardScreen(
                    viewModel = viewModel,
                    onAdjustStockClick = { product ->
                        navController.navigate("product_add_edit/${product.id}")
                    }
                )
            }

            composable(
                route = addEditProductRoute,
                arguments = listOf(navArgument("productId") { type = NavType.LongType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
                ProductAddEditScreen(
                    viewModel = viewModel,
                    productId = productId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}