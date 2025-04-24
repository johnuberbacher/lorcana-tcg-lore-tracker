package com.example.lorcanatcgloretracker.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text

@Composable
fun CounterScreen(
    left: Int,
    right: Int,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onSwipe: () -> Unit
) {
    Log.d("CounterScreen", "Rendering with $left - $right")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (offset.x < size.width / 2f) {
                        onLeftClick()
                    } else {
                        onRightClick()
                    }
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 50) onSwipe()
                }
            }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$left",
                    fontSize = 32.sp,
                    color = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$right",
                    fontSize = 32.sp,
                    color = Color.White
                )
            }
        }
    }
}
