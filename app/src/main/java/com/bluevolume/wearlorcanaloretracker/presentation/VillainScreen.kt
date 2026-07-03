// VillainScreen.kt
package com.bluevolume.wearlorcanaloretracker.presentation

import android.media.SoundPool
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bluevolume.wearlorcanaloretracker.R
import com.bluevolume.wearlorcanaloretracker.presentation.theme.MyFontFamily
import com.bluevolume.wearlorcanaloretracker.presentation.theme.SecondaryColor

private const val PLAYER_TARGET = 20
private const val VILLAIN_TARGET = 40

@Composable
fun VillainMainScreen(
    onOpenSettings: () -> Unit,
    onOpenGameover: (String) -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val selectedTheme by settingsViewModel.selectedTheme.collectAsState()
    val gameMode by settingsViewModel.gameMode.collectAsState()
    val leftCount by settingsViewModel.leftCount.collectAsState()
    val villainCount by settingsViewModel.villainCount.collectAsState()
    val ursDrawStage by settingsViewModel.ursDrawStage.collectAsState()
    val crownHolder by settingsViewModel.crownHolder.collectAsState()
    val isGameOver by settingsViewModel.isGameOver.collectAsState()

    val villainName = if (gameMode == "jaf") "Jafar" else "Ursula"
    val villainTint = if (gameMode == "jaf") Color(0xFF8B0000) else Color(0xFF4B0082)

    val context = LocalContext.current
    val soundPool = remember { SoundPool.Builder().setMaxStreams(2).build() }
    val soundGetLore = remember { soundPool.load(context, R.raw.get_lore, 1) }
    remember { soundPool.load(context, R.raw.game_complete, 1) }

    DisposableEffect(Unit) { onDispose { soundPool.release() } }

    fun triggerWin(winner: String) {
        settingsViewModel.setGameOver(true)
        Handler(Looper.getMainLooper()).postDelayed({
            onOpenGameover(winner)
            Handler(Looper.getMainLooper()).postDelayed({
                settingsViewModel.resetGame()
            }, 500)
        }, 500)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (selectedTheme == "image") {
            Image(
                painter = painterResource(
                    id = if (gameMode == "jaf") R.drawable.image_background_jaf else R.drawable.image_background_urs
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
        }

        Row(modifier = Modifier.fillMaxSize()) {
            // Player half
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                LoreCounter(
                    count = leftCount,
                    maxCount = PLAYER_TARGET,
                    onDecrease = {
                        if (!isGameOver) settingsViewModel.decrementLeft()
                    },
                    onIncrease = {
                        if (!isGameOver && leftCount < PLAYER_TARGET) {
                            settingsViewModel.incrementLeft()
                            val newLeft = leftCount + 1
                            val crownOk = gameMode != "jaf" || crownHolder == "players"
                            if (newLeft >= PLAYER_TARGET && crownOk) {
                                triggerWin("You Win!")
                            }
                        }
                    },
                    soundPool = soundPool,
                    soundGetLore = soundGetLore,
                    settingsViewModel = settingsViewModel
                )
            }

            // Villain half (tinted only in dark theme; image shows through, oled stays pure black/white)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .then(
                        if (selectedTheme == "dark") Modifier.background(villainTint.copy(alpha = 0.25f))
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                LoreCounter(
                    count = villainCount,
                    maxCount = VILLAIN_TARGET,
                    onDecrease = {
                        if (!isGameOver) settingsViewModel.decrementVillain()
                    },
                    onIncrease = {
                        if (!isGameOver && villainCount < VILLAIN_TARGET) {
                            settingsViewModel.incrementVillain()
                            val newVillain = villainCount + 1
                            if (newVillain >= VILLAIN_TARGET) {
                                triggerWin("$villainName Wins!")
                            }
                        }
                    },
                    soundPool = soundPool,
                    soundGetLore = soundGetLore,
                    settingsViewModel = settingsViewModel
                )
            }
        }

        // Top-center pill: Urs's draw counter (display only) or Jaf's crown toggle
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Box(
                modifier = Modifier
                    .padding(top = 0.dp)
                    .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                    .clip(RoundedCornerShape(topStartPercent = 0, topEndPercent = 0, bottomEndPercent = 50, bottomStartPercent = 50))
                    .then(
                        if (gameMode == "jaf") Modifier.clickable(onClick = { settingsViewModel.toggleCrownHolder() })
                        else Modifier
                    )
                    .then(
                        if (gameMode == "jaf") Modifier.padding(horizontal = 4.dp) else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (selectedTheme == "dark") Color.White.copy(alpha = 0.1f) else if (selectedTheme == "oled") Color.White.copy(
                                alpha = 0.0f
                            ) else Color.Black.copy(alpha = 0.33f)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (gameMode == "jaf") {
                            Text(
                                text = if (crownHolder == "players") "Crown: You" else "Crown: Jafar",
                                fontFamily = MyFontFamily,
                                color = if (selectedTheme == "oled") Color.White else SecondaryColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = "Draws: ${2 + ursDrawStage}",
                                fontFamily = MyFontFamily,
                                color = if (selectedTheme == "oled") Color.White else SecondaryColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Bottom-center settings icon
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(
                modifier = Modifier
                    .padding(bottom = 0.dp)
                    .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                    .clip(RoundedCornerShape(topStartPercent = 50, topEndPercent = 50, bottomEndPercent = 0, bottomStartPercent = 0))
                    .clickable(onClick = { onOpenSettings() }),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (selectedTheme == "dark") Color.White.copy(alpha = 0.1f) else if (selectedTheme == "oled") Color.White.copy(
                                alpha = 0.0f
                            ) else Color.Black.copy(alpha = 0.33f)
                        )
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = if (selectedTheme == "oled") Color.White else SecondaryColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
