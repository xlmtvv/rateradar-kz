package kz.edu.kbtu.rateradar.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kz.edu.kbtu.rateradar.R
import kz.edu.kbtu.rateradar.ui.theme.RateRadarTheme
import kz.edu.kbtu.rateradar.ui.theme.Spacing

// Small box with a label ("Buy USD") and a rate ("512.50 ₸")
@Composable
fun RateBlock(
    label: String,
    rate: Double,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.small,
    ) {
        Column(modifier = Modifier.padding(horizontal = Spacing.medium, vertical = Spacing.small)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Text(
                text = stringResource(R.string.rate_value, rate),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RateBlockPreview() {
    RateRadarTheme {
        RateBlock(label = "Buy USD", rate = 512.5)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RateBlockDarkPreview() {
    RateRadarTheme {
        RateBlock(label = "Sell USD", rate = 515.0)
    }
}
