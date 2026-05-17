package com.dobbleshop.neovision.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = NeoPrimary,
    secondary = NeoSecondary,
    tertiary = NeoAccent,
    background = NeoBackground,
    surface = NeoSurface,
    surfaceVariant = NeoSurfaceVariant,
    onPrimary = NeoSurface,
    onSecondary = NeoSurface,
    onTertiary = NeoSurface,
    onBackground = NeoText,
    onSurface = NeoText,
    onSurfaceVariant = NeoTextSecondary,
    error = StatusCritical,
    onError = NeoSurface
)

private val DarkColorScheme = darkColorScheme(
    primary = NeoPrimaryDark,
    secondary = NeoSecondary,
    tertiary = NeoAccent,
    background = NeoBackgroundDark,
    surface = NeoSurfaceDark,
    onPrimary = NeoText,
    onSecondary = NeoText,
    onTertiary = NeoText,
    onBackground = NeoTextDark,
    onSurface = NeoTextDark,
    error = StatusCritical,
    onError = NeoSurface
)

@Composable
fun DobbleShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DobbleShopTypography,
        content = content
    )
}
