package kz.edu.kbtu.rateradar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = TealPrimary,
    onPrimary = OnTealPrimary,
    primaryContainer = TealContainer,
    onPrimaryContainer = OnTealContainer,
    secondary = GraySecondary,
    onSecondary = OnGraySecondary,
    secondaryContainer = GrayContainer,
    onSecondaryContainer = OnGrayContainer,
    background = LightBackground,
    onBackground = DarkText,
    surface = LightSurface,
    onSurface = DarkText,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = GrayText,
    outline = LightOutline,
)

private val DarkColors = darkColorScheme(
    primary = TealPrimaryDark,
    onPrimary = OnTealPrimaryDark,
    primaryContainer = TealContainerDark,
    onPrimaryContainer = OnTealContainerDark,
    secondary = GraySecondaryDark,
    onSecondary = OnGraySecondaryDark,
    secondaryContainer = GrayContainerDark,
    onSecondaryContainer = OnGrayContainerDark,
    background = DarkBackground,
    onBackground = LightText,
    surface = DarkSurface,
    onSurface = LightText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = LightGrayText,
    outline = DarkOutline,
)

@Composable
fun RateRadarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content,
    )
}
