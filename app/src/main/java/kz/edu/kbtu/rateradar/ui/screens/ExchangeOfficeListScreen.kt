package kz.edu.kbtu.rateradar.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
fun ExchangeOfficeListScreen(
    uiState: RateRadarUiState,
    onSearchChange: (String) -> Unit,
    onCitySelected: (String) -> Unit,
    onOfficeClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onOpenFavorites: () -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_offices)) },
                actions = {
                    IconButton(onClick = onOpenFavorites) {
                        Icon(Icons.Filled.Favorite, contentDescription = stringResource(R.string.open_favorites))
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchChange,
                label = { Text(stringResource(R.string.search_offices)) },
                singleLine = true,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.large, vertical = Spacing.medium),
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = Spacing.large),
                horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
            ) {
                items(uiState.cities) { city ->
                    FilterChip(
                        selected = city == uiState.selectedCity,
                        onClick = { onCitySelected(city) },
                        label = { Text(city) },
                        enabled = !uiState.isLoading,
                    )
                }
            }
            when {
                uiState.isLoading -> LoadingState(modifier = Modifier.weight(1f))
                uiState.error != null -> LoadErrorState(uiState.error, onRetry, modifier = Modifier.weight(1f))
                uiState.offices.isEmpty() -> EmptyState(
                    icon = Icons.Outlined.LocationOn,
                    title = stringResource(R.string.empty_city_title),
                    message = stringResource(R.string.empty_city_message),
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                )
                else -> LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(Spacing.large),
                    verticalArrangement = Arrangement.spacedBy(Spacing.medium),
                ) {
                    items(uiState.offices, key = { it.id }) { office ->
                        OfficeCard(
                            office = office,
                            isFavorite = office.id in uiState.favoriteIds,
                            onClick = { onOfficeClick(office.id) },
                            onFavoriteClick = { onFavoriteClick(office.id) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Content", showBackground = true)
@Preview(name = "Content dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExchangeOfficeListContentPreview() {
    RateRadarTheme {
        ExchangeOfficeListScreen(PreviewData.content, {}, {}, {}, {}, {}, {})
    }
}

@Preview(name = "Loading", showBackground = true)
@Composable
private fun ExchangeOfficeListLoadingPreview() {
    RateRadarTheme {
        ExchangeOfficeListScreen(PreviewData.loading, {}, {}, {}, {}, {}, {})
    }
}

@Preview(name = "Empty", showBackground = true)
@Composable
private fun ExchangeOfficeListEmptyPreview() {
    RateRadarTheme {
        ExchangeOfficeListScreen(PreviewData.empty, {}, {}, {}, {}, {}, {})
    }
}

@Preview(name = "Error", showBackground = true)
@Composable
private fun ExchangeOfficeListErrorPreview() {
    RateRadarTheme {
        ExchangeOfficeListScreen(PreviewData.error, {}, {}, {}, {}, {}, {})
    }
}
