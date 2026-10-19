package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CyberShieldColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = CyberNavyDark,
    primaryContainer = CyberNavyElevated,
    onPrimaryContainer = CyberCyan,
    secondary = CyberTeal,
    onSecondary = CyberNavyDark,
    secondaryContainer = CyberNavyCard,
    onSecondaryContainer = CyberTextPrimary,
    tertiary = CyberGreen,
    onTertiary = CyberNavyDark,
    background = CyberNavyDark,
    onBackground = CyberTextPrimary,
    surface = CyberNavySurface,
    onSurface = CyberTextPrimary,
    surfaceVariant = CyberNavyCard,
    onSurfaceVariant = CyberTextSecondary,
    outline = CyberBorder,
    outlineVariant = CyberSurfaceHighlight,
    error = CyberRed,
    onError = CyberTextPrimary
)

@Composable
fun CyberShieldTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = CyberNavyDark.toArgb()
            window.navigationBarColor = CyberNavyDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = CyberShieldColorScheme,
        typography = Typography,
        content = content
    )
}
