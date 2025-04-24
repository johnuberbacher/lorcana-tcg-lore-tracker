// Theme.kt
package com.example.lorcanatcgloretracker.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun LorcanaTCGLoreTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = MyTypography
    ) {
        content()
    }
}