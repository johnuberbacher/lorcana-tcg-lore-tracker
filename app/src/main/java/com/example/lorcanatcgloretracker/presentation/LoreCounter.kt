// LoreCounter.kt
package com.example.lorcanatcgloretracker.presentation

import android.media.SoundPool
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.lorcanatcgloretracker.presentation.theme.MyColors
import com.example.lorcanatcgloretracker.presentation.theme.MyFontFamily

@Composable
fun LoreCounter(
    count: Int,
    maxCount: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    soundPool: SoundPool,
    soundGetLore: Int,
    settingsViewModel: SettingsViewModel
) {
    val selectedTheme by settingsViewModel.selectedTheme.collectAsState()

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        // Decrease Button
        Box(
            modifier = Modifier
                .zIndex(2f)
                .weight(1f)
                .fillMaxHeight()
                .fillMaxWidth()
                .clickable {
                    if (count > 0) {
                        soundPool.play(soundGetLore, 1f, 1f, 1, 0, 1f)
                        onDecrease()
                    }
                }
        )
        // Increase Button
        Box(
            modifier = Modifier
                .zIndex(2f)
                .weight(1f)
                .fillMaxHeight()
                .fillMaxWidth()
                .clickable {
                    if (count < maxCount) {
                        soundPool.play(soundGetLore, 1f, 1f, 1, 0, 1f)
                        onIncrease()
                    }
                }
        )

    }
    // Counter Text
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
        ) {
            Text(
                text = "-",
                fontFamily = MyFontFamily,
                color = if (selectedTheme == "oled") Color.White else MyColors.secondary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$count",
                fontFamily = MyFontFamily,
                color = if (selectedTheme == "oled") Color.White else MyColors.secondary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "+",
                fontFamily = MyFontFamily,
                color = if (selectedTheme == "oled") Color.White else MyColors.secondary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

}
