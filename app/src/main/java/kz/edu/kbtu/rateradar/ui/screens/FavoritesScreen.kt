package kz.edu.kbtu.rateradar.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kz.edu.kbtu.rateradar.R
import kz.edu.kbtu.rateradar.ui.components.EmptyState
import kz.edu.kbtu.rateradar.ui.components.LoadErrorState
import kz.edu.kbtu.rateradar.ui.components.LoadingState
import kz.edu.kbtu.rateradar.ui.components.OfficeCard
import kz.edu.kbtu.rateradar.ui.preview.PreviewData
import kz.edu.kbtu.rateradar.ui.theme.RateRadarTheme
import kz.edu.kbtu.rateradar.ui.theme.Spacing
import kz.edu.kbtu.rateradar.viewmodel.RateRadarUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    uiState: RateRadarUiState,
    onBackClick: () -> Unit,
    onOfficeClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_favorites)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
            )
        },
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when {
            uiState.isLoading -> LoadingState(modifier)
            uiState.error != null -> LoadErrorState(uiState.error, onRetry, modifier)
            uiState.favoriteOffices.isEmpty() -> EmptyState(
                icon = Icons.Outlined.FavoriteBorder,
                title = stringResource(R.string.empty_favorites_title),
                message = stringResource(R.string.empty_favorites_message),
                modifier = modifier.fillMaxSize(),
            )
            else -> LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(Spacing.large),
                verticalArrangement = Arrangement.spacedBy(Spacing.medium),
            ) {
                items(uiState.favoriteOffices, key = { it.id }) { office ->
                    OfficeCard(
                        office = office,
                        isFavorite = true,
                        onClick = { onOfficeClick(office.id) },
                        onFavoriteClick = { onFavoriteClick(office.id) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview(name = "Content", showBackground = true)
@Preview(name = "Content dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FavoritesContentPreview() {
    RateRadarTheme { FavoritesScreen(PreviewData.content, {}, {}, {}, {}) }
}

@Preview(name = "Loading", showBackground = true)
@Composable
private fun FavoritesLoadingPreview() {
    RateRadarTheme { FavoritesScreen(PreviewData.loading, {}, {}, {}, {}) }
}

@Preview(name = "Empty", showBackground = true)
@Composable
private fun FavoritesEmptyPreview() {
    RateRadarTheme { FavoritesScreen(PreviewData.empty, {}, {}, {}, {}) }
}

@Preview(name = "Error", showBackground = true)
@Composable
private fun FavoritesErrorPreview() {
    RateRadarTheme { FavoritesScreen(PreviewData.error, {}, {}, {}, {}) }
}
