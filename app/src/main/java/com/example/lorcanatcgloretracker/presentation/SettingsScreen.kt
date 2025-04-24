package com.example.lorcanatcgloretracker.presentation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SettingsScreen(
    max: Int,
    toggleMax: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = toggleMax) {
            Text("Set Max: ${if (max == 20) "25" else "20"}")
        }
    }
}
