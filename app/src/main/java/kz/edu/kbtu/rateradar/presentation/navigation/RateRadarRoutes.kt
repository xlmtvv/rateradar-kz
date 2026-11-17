package kz.edu.kbtu.rateradar.presentation.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kz.edu.kbtu.rateradar.presentation.notifications.canShowRateReminder
import kz.edu.kbtu.rateradar.presentation.notifications.showRateReminderNotification
import kz.edu.kbtu.rateradar.presentation.screens.ExchangeOfficeListScreen
import kz.edu.kbtu.rateradar.presentation.screens.FavoritesScreen
import kz.edu.kbtu.rateradar.presentation.screens.OfficeDetailsScreen
import kz.edu.kbtu.rateradar.presentation.RateRadarViewModel

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
    val appContext = LocalContext.current.applicationContext
    var showNotificationError by rememberSaveable { mutableStateOf(false) }
    var isRequestingPermission by rememberSaveable { mutableStateOf(false) }

    fun startReminder() {
        showNotificationError = !canShowRateReminder(appContext)
        if (!showNotificationError) {
            viewModel.startRateReminder { rate -> showRateReminderNotification(appContext, rate) }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        isRequestingPermission = false
        if (granted) startReminder() else showNotificationError = true
    }

    LaunchedEffect(officeId) {
        viewModel.onOfficeSelected(officeId)
    }
    OfficeDetailsScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onFavoriteClick = viewModel::onFavoriteClick,
        onRetry = viewModel::onRetry,
        showNotificationError = showNotificationError,
        onCancelRateReminder = viewModel::cancelRateReminder,
        onRateReminderClick = click@{
            if (isRequestingPermission) return@click

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(appContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
            ) {
                showNotificationError = false
                isRequestingPermission = true
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                startReminder()
            }
        },
    )
}
