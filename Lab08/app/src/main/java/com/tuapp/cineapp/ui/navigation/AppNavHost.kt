package com.tuapp.cineapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tuapp.cineapp.ui.detail.DetailScreen
import com.tuapp.cineapp.ui.favorites.FavoritesScreen
import com.tuapp.cineapp.ui.home.HomeScreen
import com.tuapp.cineapp.ui.search.SearchScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(onMovieClick = { navController.navigate(Screen.Detail.createRoute(it)) })
        }
        composable(Screen.Search.route) {
            SearchScreen(onMovieClick = { navController.navigate(Screen.Detail.createRoute(it)) })
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(onMovieClick = { navController.navigate(Screen.Detail.createRoute(it)) })
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) {
            DetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
