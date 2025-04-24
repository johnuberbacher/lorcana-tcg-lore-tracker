// CounterSide.kt
package com.example.lorcanatcgloretracker.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text

@Composable
fun CounterSide(count: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$count", color = Color.White, fontSize = 24.sp)
    }
}