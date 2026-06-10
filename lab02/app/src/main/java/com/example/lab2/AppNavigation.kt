package com.example.lab2

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab2.data.UserPreferencesManager
import com.example.lab2.ui.HabitDetailScreen
import com.example.lab2.ui.LoginScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPreferencesManager = remember { UserPreferencesManager(context) }
    val coroutineScope = rememberCoroutineScope()
    
    // Obtenemos el nombre de usuario de DataStore
    val username by userPreferencesManager.userNameFlow.collectAsState(initial = null)
    
    // Determinamos la ruta inicial de forma dinámica
    // Usamos un pequeño delay o comprobamos null para evitar saltos raros, 
    // pero para simplicidad, si es null o vacío, vamos a Login.
    val startDestination = if (username.isNullOrBlank()) {
        ScreenRoute.Login.route
    } else {
        ScreenRoute.Home.route
    }

    val habitViewModel: HabitViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Login.route // Empezamos en Login y redirigimos si es necesario
    ) {
        
        composable(ScreenRoute.Login.route) {
            // Si ya hay usuario, saltamos al Home y evitamos mostrar el Login
            LaunchedEffect(username) {
                if (!username.isNullOrBlank()) {
                    navController.navigate(ScreenRoute.Home.route) {
                        popUpTo(ScreenRoute.Login.route) { inclusive = true }
                    }
                }
            }
            
            LoginScreen(
                onLoginSuccess = { name ->
                    coroutineScope.launch {
                        userPreferencesManager.saveUsername(name)
                        // La navegación se hará sola gracias al LaunchedEffect de arriba
                    }
                }
            )
        }

        composable(ScreenRoute.Home.route) {
            HomeScreen(
                username = username ?: "Usuario",
                viewModel = habitViewModel,
                onHabitClick = { habitId ->
                    navController.navigate(ScreenRoute.HabitDetail.createRoute(habitId))
                },
                onLogout = {
                    coroutineScope.launch {
                        userPreferencesManager.clearUsername()
                        navController.navigate(ScreenRoute.Login.route) {
                            popUpTo(ScreenRoute.Home.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(
            route = ScreenRoute.HabitDetail.route,
            arguments = listOf(navArgument("habitId") { type = NavType.IntType })
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getInt("habitId") ?: 0
            HabitDetailScreen(
                habitId = habitId,
                viewModel = habitViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
