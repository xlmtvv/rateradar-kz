package kz.edu.kbtu.rateradar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kz.edu.kbtu.rateradar.ui.screens.ExchangeOfficeListScreen
import kz.edu.kbtu.rateradar.ui.screens.FavoritesScreen
import kz.edu.kbtu.rateradar.ui.screens.OfficeDetailsScreen
import kz.edu.kbtu.rateradar.viewmodel.RateRadarViewModel

@Composable
fun ExchangeOfficeListRoute(
    viewModel: RateRadarViewModel,
    onOfficeClick: (Int) -> Unit,
    onOpenFavorites: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ExchangeOfficeListScreen(
        uiState = uiState,
        onSearchChange = viewModel::onSearchChange,
        onCitySelected = viewModel::onCitySelected,
        onOfficeClick = { id ->
            viewModel.onOfficeSelected(id)
            onOfficeClick(id)
        },
        onFavoriteClick = viewModel::onFavoriteClick,
        onOpenFavorites = onOpenFavorites,
        onRetry = viewModel::onRetry,
    )
}

@Composable
fun FavoritesRoute(
    viewModel: RateRadarViewModel,
    onBackClick: () -> Unit,
    onOfficeClick: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FavoritesScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onOfficeClick = { id ->
            viewModel.onOfficeSelected(id)
            onOfficeClick(id)
        },
        onFavoriteClick = viewModel::onFavoriteClick,
        onRetry = viewModel::onRetry,
    )
}

@Composable
fun OfficeDetailsRoute(officeId: Int, viewModel: RateRadarViewModel, onBackClick: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(officeId) {
        viewModel.onOfficeSelected(officeId)
    }
    OfficeDetailsScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onFavoriteClick = viewModel::onFavoriteClick,
        onRetry = viewModel::onRetry,
    )
}
