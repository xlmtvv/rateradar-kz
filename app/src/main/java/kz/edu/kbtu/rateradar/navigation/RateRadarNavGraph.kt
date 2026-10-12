package kz.edu.kbtu.rateradar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kz.edu.kbtu.rateradar.ui.screens.ExchangeOfficeListScreen
import kz.edu.kbtu.rateradar.ui.screens.FavoritesScreen
import kz.edu.kbtu.rateradar.ui.screens.OfficeDetailsScreen

object Routes {
    const val OFFICES = "offices"
    const val DETAILS = "details/{officeId}"
    const val FAVORITES = "favorites"

    fun details(officeId: Int) = "details/$officeId"
}

@Composable
fun RateRadarNavGraph() {
    val navController = rememberNavController()

    var favoriteIds by remember { mutableStateOf(setOf<Int>()) }

    fun toggleFavorite(officeId: Int) {
        favoriteIds = if (officeId in favoriteIds) {
            favoriteIds - officeId
        } else {
            favoriteIds + officeId
        }
    }

    NavHost(navController = navController, startDestination = Routes.OFFICES) {

        composable(Routes.OFFICES) {
            ExchangeOfficeListScreen(
                favoriteIds = favoriteIds,
                onOfficeClick = { id -> navController.navigate(Routes.details(id)) },
                onFavoriteClick = { id -> toggleFavorite(id) },
                onOpenFavorites = { navController.navigate(Routes.FAVORITES) },
            )
        }

        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument("officeId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val officeId = backStackEntry.arguments?.getInt("officeId") ?: 0
            OfficeDetailsScreen(
                officeId = officeId,
                isFavorite = officeId in favoriteIds,
                onBackClick = { navController.popBackStack() },
                onFavoriteClick = { id -> toggleFavorite(id) },
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                favoriteIds = favoriteIds,
                onBackClick = { navController.popBackStack() },
                onOfficeClick = { id -> navController.navigate(Routes.details(id)) },
                onFavoriteClick = { id -> toggleFavorite(id) },
            )
        }
    }
}
