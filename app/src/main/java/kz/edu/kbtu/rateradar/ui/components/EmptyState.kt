package kz.edu.kbtu.rateradar.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import kz.edu.kbtu.rateradar.ui.theme.RateRadarTheme
import kz.edu.kbtu.rateradar.ui.theme.Sizes
import kz.edu.kbtu.rateradar.ui.theme.Spacing

@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(Spacing.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(Sizes.emptyStateIcon),
            tint = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = title,
            modifier = Modifier.padding(top = Spacing.large),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )



        Text(
            text = message,
            modifier = Modifier.padding(top = Spacing.medium),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStatePreview() {
    RateRadarTheme {
        EmptyState(
            icon = Icons.Outlined.FavoriteBorder,
            title = "No favorites yet",
            message = "Tap the heart on an office to save it here.",
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EmptyStateDarkPreview() {
    RateRadarTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            EmptyState(
                icon = Icons.Outlined.FavoriteBorder,
                title = "No favorites yet",
                message = "Tap the heart on an office to save it here.",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
