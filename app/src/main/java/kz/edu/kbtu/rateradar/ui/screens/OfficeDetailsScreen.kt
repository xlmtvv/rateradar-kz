package kz.edu.kbtu.rateradar.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kz.edu.kbtu.rateradar.R
import kz.edu.kbtu.rateradar.data.SampleData
import kz.edu.kbtu.rateradar.ui.components.EmptyState
import kz.edu.kbtu.rateradar.ui.components.RateBlock
import kz.edu.kbtu.rateradar.ui.theme.RateRadarTheme
import kz.edu.kbtu.rateradar.ui.theme.Sizes
import kz.edu.kbtu.rateradar.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfficeDetailsScreen(
    officeId: Int,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: (Int) -> Unit,
) {
    val office = SampleData.offices.find { it.id == officeId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_details)) },
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
        if (office == null) {
            EmptyState(
                icon = Icons.Outlined.Info,
                title = stringResource(R.string.office_not_found_title),
                message = stringResource(R.string.office_not_found_message),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.large),
                verticalArrangement = Arrangement.spacedBy(Spacing.large),
            ) {
                Image(
                    painter = painterResource(R.drawable.exchange_placeholder),
                    contentDescription = stringResource(R.string.office_image_description, office.name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Sizes.detailsImageHeight)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop,
                )

                Text(
                    text = office.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Column(
                        modifier = Modifier.padding(Spacing.large),
                        verticalArrangement = Arrangement.spacedBy(Spacing.small),
                    ) {
                        Text(
                            text = stringResource(R.string.label_city, office.city),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = stringResource(R.string.label_address, office.address),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = stringResource(R.string.label_hours, office.workingHours),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.medium)) {
                    RateBlock(
                        label = stringResource(R.string.buy),
                        rate = office.usdBuy,
                        modifier = Modifier.weight(1f),
                    )
                    RateBlock(
                        label = stringResource(R.string.sell),
                        rate = office.usdSell,
                        modifier = Modifier.weight(1f),
                    )
                }

                val buttonModifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = Sizes.minTouchTarget)
                if (isFavorite) {
                    OutlinedButton(onClick = { onFavoriteClick(office.id) }, modifier = buttonModifier) {
                        Text(stringResource(R.string.remove_from_favorites))
                    }
                } else {
                    Button(onClick = { onFavoriteClick(office.id) }, modifier = buttonModifier) {
                        Text(stringResource(R.string.add_to_favorites))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OfficeDetailsScreenPreview() {
    RateRadarTheme {
        OfficeDetailsScreen(
            officeId = 1,
            isFavorite = false,
            onBackClick = {},
            onFavoriteClick = {},
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OfficeDetailsScreenDarkPreview() {
    RateRadarTheme {
        OfficeDetailsScreen(
            officeId = 3,
            isFavorite = true,
            onBackClick = {},
            onFavoriteClick = {},
        )
    }
}
