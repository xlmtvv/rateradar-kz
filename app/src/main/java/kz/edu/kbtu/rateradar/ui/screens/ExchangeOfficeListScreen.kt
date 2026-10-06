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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kz.edu.kbtu.rateradar.R
import kz.edu.kbtu.rateradar.data.SampleData
import kz.edu.kbtu.rateradar.ui.components.EmptyState
import kz.edu.kbtu.rateradar.ui.components.OfficeCard
import kz.edu.kbtu.rateradar.ui.theme.RateRadarTheme
import kz.edu.kbtu.rateradar.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeOfficeListScreen(
    favoriteIds: Set<Int>,
    onOfficeClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onOpenFavorites: () -> Unit,
) {
    // Selected city chip. rememberSaveable keeps it when we come back from another screen.
    var selectedCity by rememberSaveable { mutableStateOf(SampleData.ALL_CITIES) }

    val offices = if (selectedCity == SampleData.ALL_CITIES) {
        SampleData.offices
    } else {
        SampleData.offices.filter { it.city == selectedCity }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_offices)) },
                actions = {
                    IconButton(onClick = onOpenFavorites) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = stringResource(R.string.open_favorites),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Horizontal list of city filters
            LazyRow(
                contentPadding = PaddingValues(horizontal = Spacing.large),
                horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
            ) {
                items(SampleData.cities) { city ->
                    FilterChip(
                        selected = city == selectedCity,
                        onClick = { selectedCity = city },
                        label = { Text(city) },
                    )
                }
            }

            if (offices.isEmpty()) {
                EmptyState(
                    icon = Icons.Outlined.LocationOn,
                    title = stringResource(R.string.empty_city_title),
                    message = stringResource(R.string.empty_city_message),
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(Spacing.large),
                    verticalArrangement = Arrangement.spacedBy(Spacing.medium),
                ) {
                    items(offices, key = { it.id }) { office ->
                        OfficeCard(
                            office = office,
                            isFavorite = office.id in favoriteIds,
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

@Preview(showBackground = true)
@Composable
private fun ExchangeOfficeListScreenPreview() {
    RateRadarTheme {
        ExchangeOfficeListScreen(
            favoriteIds = setOf(1, 3),
            onOfficeClick = {},
            onFavoriteClick = {},
            onOpenFavorites = {},
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExchangeOfficeListScreenDarkPreview() {
    RateRadarTheme {
        ExchangeOfficeListScreen(
            favoriteIds = setOf(2),
            onOfficeClick = {},
            onFavoriteClick = {},
            onOpenFavorites = {},
        )
    }
}
