// Theme.kt
package com.example.lorcanatcgloretracker.presentation.theme

import androidx.compose.material3.MaterialTheme // Material 3 Theme
import androidx.compose.material3.darkColorScheme // Material 3 color scheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MyColorScheme = darkColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = DarkestColor,
    surface = AccentColor,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary,
    onBackground = OnDarkest,
    onSurface = OnAccent,
    error = Color.Red,
    onError = Color.White
)

@Composable
fun LorcanaTCGLoreTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MyColorScheme,
        typography = MyTypography
    ) {
        content()
    }
}
