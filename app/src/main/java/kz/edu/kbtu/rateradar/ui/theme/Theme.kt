package kz.edu.kbtu.rateradar.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val ColorWhite = androidx.compose.ui.graphics.Color.White

private val LightColors = lightColorScheme(
    primary = Teal,
    onPrimary = ColorWhite,
    primaryContainer = LightTeal,
    onPrimaryContainer = Navy,
    background = Ice,
    onBackground = Navy,
    surface = Ice,
    onSurface = Navy,
    onSurfaceVariant = Slate,
)

private val DarkColors = darkColorScheme(
    primary = LightTeal,
    onPrimary = Navy,
    primaryContainer = Teal,
    onPrimaryContainer = ColorWhite,
)

@Composable
fun RateRadarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content,
    )
}
