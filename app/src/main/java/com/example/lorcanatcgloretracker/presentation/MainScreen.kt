package com.example.lorcanatcgloretracker.presentation

import android.media.SoundPool
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.compose.rememberNavController
import com.example.lorcanatcgloretracker.R
import com.example.lorcanatcgloretracker.presentation.theme.MyColors
import com.example.lorcanatcgloretracker.presentation.theme.MyFontFamily

@Composable
fun MainScreen(onOpenSettings: () -> Unit, settingsViewModel: SettingsViewModel) {
    val navController = rememberNavController()
    val selectedTheme by settingsViewModel.selectedTheme.collectAsState()

    var maxLoreCount by remember { mutableIntStateOf(20) }
    var leftCount by remember { mutableIntStateOf(0) }
    var rightCount by remember { mutableIntStateOf(0) }

    var volume by remember { mutableFloatStateOf(1f) } // Volume from 0.0 to 1.0
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    // Setup SoundPool
    val soundPool = remember {
        SoundPool.Builder().setMaxStreams(2).build()
    }

    val soundGetLore = remember {
        soundPool.load(context, R.raw.get_lore, 1)
    }

    val soundGameWon = remember {
        soundPool.load(context, R.raw.game_complete, 1)
    }

    val loreValues = listOf(20, 25, 10, 15)

    var loreIndex by remember { mutableIntStateOf(0) }

    fun cycleMaxLoreCount() {
        loreIndex = (loreIndex + 1) % loreValues.size
        maxLoreCount = loreValues[loreIndex]
    }

    DisposableEffect(Unit) {
        onDispose {
            soundPool.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (selectedTheme == "image") {
            Image(
                painter = painterResource(id = R.drawable.background),
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

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Left Half
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                LoreCounter(
                    count = leftCount,
                    maxCount = maxLoreCount,
                    onDecrease = { leftCount-- },
                    onIncrease = { leftCount++ },
                    soundPool = soundPool,
                    soundGetLore = soundGetLore,
                    settingsViewModel = settingsViewModel
                )
            }

            // Right Half
            Box(
                modifier = Modifier
                    .background(
                        if (selectedTheme == "dark") Color.White.copy(alpha = 0.033f)
                        else if (selectedTheme == "oled") Color.White.copy(alpha = 0.0f)
                        else Color.Black.copy(alpha = 0.5f)
                    )
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                LoreCounter(
                    count = rightCount,
                    maxCount = maxLoreCount,
                    onDecrease = { rightCount-- },
                    onIncrease = {
                        rightCount++
                        if (rightCount >= maxLoreCount) {
                            soundPool.play(soundGameWon, 1f, 1f, 1, 0, 1f)
                        }
                    },
                    soundPool = soundPool,
                    soundGetLore = soundGetLore,
                    settingsViewModel = settingsViewModel
                )
            }
        }


        // Total Lore Count
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (selectedTheme == "dark") Color.White.copy(alpha = 0.1f)
                        else if (selectedTheme == "oled") Color.White.copy(alpha = 0.0f)
                        else Color.Black.copy(alpha = 0.1f)
                    )
                    .clickable(
                        onClick = { cycleMaxLoreCount() },
                    )
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.lore_gold),
                            contentDescription = "bar action image",
                            modifier = Modifier
                                .size(15.dp), // image height,
                            tint = if (selectedTheme == "oled") Color.White else Color.Unspecified
                        )
                    }
                    Box(
                        modifier = Modifier.padding(bottom = 1.dp)
                    ) {
                        Text(
                            text = "$maxLoreCount",
                            textAlign = TextAlign.Center,
                            fontFamily = MyFontFamily,
                            color = if (selectedTheme == "oled") Color.White else MyColors.secondary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
        }

        // Settings Icon - Bottom Center (unchanged)
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (selectedTheme == "dark") Color.White.copy(alpha = 0.1f)
                        else if (selectedTheme == "oled") Color.White.copy(alpha = 0.0f)
                        else Color.Black.copy(alpha = 0.1f)
                    )
                    .clickable(
                        onClick = { onOpenSettings() },
                        role = null // You can add Role.Button if this is a button
                    )
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = if (selectedTheme == "oled") Color.White else MyColors.secondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
