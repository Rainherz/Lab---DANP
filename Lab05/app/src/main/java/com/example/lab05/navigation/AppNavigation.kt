package com.example.lab05.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.lab05.ui.components.AppToolbar
import com.example.lab05.ui.screens.*
import com.example.lab05.ui.theme.AppThemeMode
import com.example.lab05.ui.viewmodel.StoreViewModel

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Tienda")
    object Favorites : Screen("favorites", "Mis Favoritos")
    object Cart : Screen("cart", "Mi Carrito")
    object Detail : Screen("detail/{productId}", "Detalle")
}

@Composable
fun AppNavigation(
    viewModel: StoreViewModel,
    onThemeChange: (AppThemeMode) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val cartCount by viewModel.cartCount.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    var showThemeMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            val isTabScreen = currentRoute in listOf(Screen.Home.route, Screen.Favorites.route, Screen.Cart.route)
            if (isTabScreen) {
                AppToolbar(
                    title = when (currentRoute) {
                        Screen.Favorites.route -> Screen.Favorites.title
                        Screen.Cart.route -> Screen.Cart.title
                        else -> Screen.Home.title
                    },
                    actions = {
                        // Dark mode toggle
                        IconButton(onClick = { viewModel.toggleDarkMode() }) {
                            Text(
                                text = if (isDarkMode) "☀️" else "🌙",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        
                        // Theme switching dropdown
                        Box {
                            IconButton(onClick = { showThemeMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.Palette,
                                    contentDescription = "Cambiar tema"
                                )
                            }
                            DropdownMenu(
                                expanded = showThemeMenu,
                                onDismissRequest = { showThemeMenu = false }
                            ) {
                                AppThemeMode.entries.forEach { mode ->
                                    DropdownMenuItem(
                                        text = { 
                                            Text(
                                                text = when (mode) {
                                                    AppThemeMode.BLUE -> "Azul"
                                                    AppThemeMode.GREEN -> "Verde"
                                                    AppThemeMode.PURPLE -> "Púrpura"
                                                    AppThemeMode.CYBERPUNK -> "Cyberpunk Neon"
                                                },
                                                fontWeight = if (themeMode == mode) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        onClick = {
                                            viewModel.setThemeMode(mode)
                                            onThemeChange(mode)
                                            showThemeMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            val isTabScreen = currentRoute in listOf(Screen.Home.route, Screen.Favorites.route, Screen.Cart.route)
            if (isTabScreen) {
                NavigationBar {
                    // Home Tab
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Tienda") },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Home.route } == true,
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

                    // Favorites Tab
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                        label = { Text("Favoritos") },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Favorites.route } == true,
                        onClick = {
                            navController.navigate(Screen.Favorites.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

                    // Cart Tab with reactive item counter badge
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (cartCount > 0) {
                                        Badge {
                                            Text(text = cartCount.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = null)
                            }
                        },
                        label = { Text("Carrito") },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Cart.route } == true,
                        onClick = {
                            navController.navigate(Screen.Cart.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(Screen.Cart.route) {
                CartScreen(
                    viewModel = viewModel
                )
            }
            composable(Screen.Detail.route) { backStackEntry ->
                val productIdStr = backStackEntry.arguments?.getString("productId") ?: ""
                val productId = productIdStr.toIntOrNull() ?: 1
                DetailScreen(
                    productId = productId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}