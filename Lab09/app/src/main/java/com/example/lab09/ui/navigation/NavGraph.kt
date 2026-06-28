package com.example.lab09.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.lab09.ui.detail.CoinDetailScreen
import com.example.lab09.ui.list.CoinListScreen
import com.example.lab09.ui.list.CoinListViewModel
import com.example.lab09.ui.list.CoinListViewModelFactory

sealed class Screen(val route: String) {
    data object CoinList : Screen("coin_list")
    data object CoinDetail : Screen("coin_detail/{coinId}") {
        fun createRoute(coinId: String) = "coin_detail/$coinId"
    }
}

@Composable
fun CryptoNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.CoinList.route,
        modifier = modifier
    ) {
        composable(Screen.CoinList.route) {
            val viewModel: CoinListViewModel = viewModel(
                factory = CoinListViewModelFactory(context)
            )
            CoinListScreen(
                viewModel = viewModel,
                onCoinClick = { coinId ->
                    navController.navigate(Screen.CoinDetail.createRoute(coinId))
                }
            )
        }

        composable(
            route = Screen.CoinDetail.route,
            arguments = listOf(
                navArgument("coinId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val coinId = backStackEntry.arguments?.getString("coinId") ?: return@composable
            CoinDetailScreen(
                coinId = coinId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
