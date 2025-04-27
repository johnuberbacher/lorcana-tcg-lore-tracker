package com.example.lorcanatcgloretracker.presentation

import android.media.SoundPool
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lorcanatcgloretracker.R
import com.example.lorcanatcgloretracker.presentation.theme.MyFontFamily
import com.example.lorcanatcgloretracker.presentation.theme.SecondaryColor

@Composable
fun MainScreen(
    onOpenSettings: () -> Unit,
    onOpenGameover: (String) -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val selectedTheme by settingsViewModel.selectedTheme.collectAsState()

    var maxLoreCount by remember { mutableIntStateOf(20) }
    var leftCount by remember { mutableIntStateOf(0) }
    var rightCount by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    val context = LocalContext.current
    rememberCoroutineScope()
    val soundPool = remember { SoundPool.Builder().setMaxStreams(2).build() }
    val soundGetLore = remember { soundPool.load(context, R.raw.get_lore, 1) }
    remember { soundPool.load(context, R.raw.game_complete, 1) }

    val loreValues = listOf(20, 25, 10, 15)
    var loreIndex by remember { mutableIntStateOf(0) }

    fun cycleMaxLoreCount() {
        loreIndex = (loreIndex + 1) % loreValues.size
        maxLoreCount = loreValues[loreIndex]
    }

    DisposableEffect(Unit) { onDispose { soundPool.release() } }

    Box(modifier = Modifier.fillMaxSize()) {
        if (selectedTheme == "image") {
            Image(
                painter = painterResource(id = R.drawable.image_background),
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
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                LoreCounter(
                    count = leftCount,
                    maxCount = maxLoreCount,
                    onDecrease = {
                        if (!isGameOver) {
                            leftCount--
                        }
                    },
                    onIncrease = {
                        if (!isGameOver && leftCount < maxLoreCount) {
                            leftCount++
                            if (leftCount >= maxLoreCount) {
                                isGameOver = true

                                // Add a 1-second delay before calling onOpenGameover
                                Handler(Looper.getMainLooper()).postDelayed({
                                    onOpenGameover("Player 1 Wins!")

                                    // Add another 1-second delay before resetting leftCount
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        leftCount = 0
                                        rightCount = 0
                                        maxLoreCount = 20

                                        // Reset game state
                                        isGameOver = false
                                    }, 500)  // 1000 milliseconds = 1 second
                                }, 500)  // 500 milliseconds delay before gameover
                            }
                        }
                    },
                    soundPool = soundPool,
                    soundGetLore = soundGetLore,
                    settingsViewModel = settingsViewModel
                )
            }

            Box(
                modifier = Modifier
                    .background(
                        if (selectedTheme == "dark") Color.White.copy(alpha = 0.033f) else if (selectedTheme == "oled") Color.White.copy(
                            alpha = 0.0f
                        ) else Color.Black.copy(alpha = 0.25f)
                    )
                    .weight(1f)
                    .fillMaxHeight(), contentAlignment = Alignment.Center
            ) {
                LoreCounter(
                    count = rightCount,
                    maxCount = maxLoreCount,
                    onDecrease = {
                        if (!isGameOver) {
                            rightCount--
                        }
                    },
                    onIncrease = {
                        if (!isGameOver && rightCount < maxLoreCount) {
                            rightCount++
                            if (rightCount >= maxLoreCount) {
                                isGameOver = true

                                // Add a 1-second delay before calling onOpenGameover
                                Handler(Looper.getMainLooper()).postDelayed({
                                    onOpenGameover("Player 2 Wins!")

                                    // Add another 1-second delay before resetting leftCount
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        leftCount = 0
                                        rightCount = 0
                                        maxLoreCount = 20

                                        // Reset game state
                                        isGameOver = false
                                    }, 500)  // 1000 milliseconds = 1 second
                                }, 500)  // 500 milliseconds delay before gameover
                            }
                        }
                    },
                    soundPool = soundPool,
                    soundGetLore = soundGetLore,
                    settingsViewModel = settingsViewModel
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (selectedTheme == "dark") Color.White.copy(alpha = 0.1f) else if (selectedTheme == "oled") Color.White.copy(
                            alpha = 0.0f
                        ) else Color.Black.copy(alpha = 0.33f)
                    )
                    .clickable(onClick = { cycleMaxLoreCount() })
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.lore_gold),
                        contentDescription = "bar action image",
                        modifier = Modifier.size(15.dp),
                        tint = if (selectedTheme == "oled") Color.White else Color.Unspecified
                    )
                    Text(
                        text = "$maxLoreCount",
                        textAlign = TextAlign.Center,
                        fontFamily = MyFontFamily,
                        color = if (selectedTheme == "oled") Color.White else SecondaryColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (selectedTheme == "dark") Color.White.copy(alpha = 0.1f) else if (selectedTheme == "oled") Color.White.copy(
                            alpha = 0.0f
                        ) else Color.Black.copy(alpha = 0.33f)
                    )
                    .clickable(onClick = { onOpenSettings() })
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
