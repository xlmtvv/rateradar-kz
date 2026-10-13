package kz.edu.kbtu.rateradar.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kz.edu.kbtu.rateradar.viewmodel.RateRadarViewModel

object Routes {
    const val OFFICES = "offices"
    const val DETAILS = "details/{officeId}"
    const val FAVORITES = "favorites"

    fun details(officeId: Int) = "details/$officeId"
}

@Composable
fun RateRadarNavGraph() {
    val navController = rememberNavController()
    val viewModel: RateRadarViewModel = viewModel(factory = RateRadarViewModel.Factory)

    NavHost(navController = navController, startDestination = Routes.OFFICES) {
        composable(Routes.OFFICES) {
            ExchangeOfficeListRoute(
                viewModel = viewModel,
                onOfficeClick = { id -> navController.navigate(Routes.details(id)) },
                onOpenFavorites = { navController.navigate(Routes.FAVORITES) },
            )
        }
        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument("officeId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val officeId = backStackEntry.arguments?.getInt("officeId") ?: 0
            OfficeDetailsRoute(
                officeId = officeId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
            )
        }
        composable(Routes.FAVORITES) {
            FavoritesRoute(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onOfficeClick = { id -> navController.navigate(Routes.details(id)) },
            )
        }
    }
}
