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
import kz.edu.kbtu.rateradar.data.SampleData
import kz.edu.kbtu.rateradar.ui.components.EmptyState
import kz.edu.kbtu.rateradar.ui.components.OfficeCard
import kz.edu.kbtu.rateradar.ui.theme.RateRadarTheme
import kz.edu.kbtu.rateradar.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favoriteIds: Set<Int>,
    onBackClick: () -> Unit,
    onOfficeClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
) {
    val favorites = SampleData.offices.filter { it.id in favoriteIds }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_favorites)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        if (favorites.isEmpty()) {
            EmptyState(
                icon = Icons.Outlined.FavoriteBorder,
                title = stringResource(R.string.empty_favorites_title),
                message = stringResource(R.string.empty_favorites_message),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(Spacing.large),
                verticalArrangement = Arrangement.spacedBy(Spacing.medium),
            ) {
                items(favorites, key = { it.id }) { office ->
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

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreview() {
    RateRadarTheme {
        FavoritesScreen(
            favoriteIds = setOf(1, 3, 6),
            onBackClick = {},
            onOfficeClick = {},
            onFavoriteClick = {},
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FavoritesScreenEmptyDarkPreview() {
    RateRadarTheme {
        FavoritesScreen(
            favoriteIds = emptySet(),
            onBackClick = {},
            onOfficeClick = {},
            onFavoriteClick = {},
        )
    }
}
