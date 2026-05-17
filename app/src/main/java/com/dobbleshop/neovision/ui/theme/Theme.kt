package com.dobbleshop.neovision.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = NeoPrimary,
    onPrimary = NeoSurface,
    primaryContainer = NeoPrimaryLight,
    onPrimaryContainer = NeoPrimaryDark,
    
    secondary = NeoAccent,
    onSecondary = NeoSurface,
    secondaryContainer = NeoSecondary,
    onSecondaryContainer = NeoPrimaryDark,
    
    tertiary = StatusOK,
    onTertiary = NeoSurface,
    
    error = StatusCritical,
    onError = NeoSurface,
    
    background = NeoBackground,
    onBackground = NeoText,
    
    surface = NeoSurface,
    onSurface = NeoText,
    surfaceVariant = NeoSurfaceVariant,
    onSurfaceVariant = NeoTextSecondary,
    
    outline = Color(0xFFE0E0E0),
    outlineVariant = Color(0xFFF5F5F5)
)

private val DarkColorScheme = darkColorScheme(
    primary = NeoPrimary,
    onPrimary = NeoSurface,
    primaryContainer = NeoPrimaryDark,
    onPrimaryContainer = NeoPrimaryLight,
    
    secondary = NeoAccent,
    onSecondary = NeoSurface,
    secondaryContainer = NeoPrimaryDark,
    onSecondaryContainer = NeoSecondary,
    
    tertiary = StatusOK,
    onTertiary = NeoSurface,
    
    error = StatusCritical,
    onError = NeoSurface,
    
    background = NeoBackgroundDark,
    onBackground = NeoTextDark,
    
    surface = NeoSurfaceDark,
    onSurface = NeoTextDark,
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFBDBDBD),
    
    outline = Color(0xFF424242),
    outlineVariant = Color(0xFF616161)
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
