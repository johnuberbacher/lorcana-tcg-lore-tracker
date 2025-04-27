// SettingsScreen.kt
package com.example.lorcanatcgloretracker.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.ScalingLazyListAnchorType
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.RadioButton
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import com.example.lorcanatcgloretracker.R
import com.example.lorcanatcgloretracker.presentation.theme.DarkestColor
import com.example.lorcanatcgloretracker.presentation.theme.SecondaryColor

@Composable
fun SettingsScreen(onBack: () -> Unit, settingsViewModel: SettingsViewModel) {
    val selectedTheme by settingsViewModel.selectedTheme.collectAsState()
    val muted by settingsViewModel.muted.collectAsState()

    val scrollState = rememberScalingLazyListState()

    LaunchedEffect(scrollState) {
        scrollState.scrollToItem(0) // Ensure scroll to top on load
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        positionIndicator = { PositionIndicator(scalingLazyListState = scrollState) }
    ) {
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
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scrollState,
                flingBehavior = ScalingLazyColumnDefaults.snapFlingBehavior(state = scrollState),
                anchorType = ScalingLazyListAnchorType.ItemStart
            ) {
                item { ThemeSettings(selectedTheme, settingsViewModel) }
                item { SoundSettings(muted, settingsViewModel) }
                item { BackButton(onBack, selectedTheme) }
            }
        }
    }
}


@Composable
fun ThemeSettings(selectedTheme: String, settingsViewModel: SettingsViewModel) {
    val isRound = LocalConfiguration.current.isScreenRound

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            "Theme Style",
            style = MaterialTheme.typography.labelSmall,
            textAlign = if (isRound) TextAlign.Center else TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        )
        ThemeToggleChip(
            label = "Image",
            selected = selectedTheme == "image",
            onClick = { settingsViewModel.setTheme("image") })
        ThemeToggleChip(
            label = "Dark",
            selected = selectedTheme == "dark",
            onClick = { settingsViewModel.setTheme("dark") })
        ThemeToggleChip(
            label = "OLED",
            selected = selectedTheme == "oled",
            onClick = { settingsViewModel.setTheme("oled") })
    }
}

@Composable
fun SoundSettings(muted: Boolean, settingsViewModel: SettingsViewModel) {
    val isRound = LocalConfiguration.current.isScreenRound

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            "Sound",
            style = MaterialTheme.typography.labelSmall,
            textAlign = if (isRound) TextAlign.Center else TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        )
        MuteSoundToggleChip(selected = muted, onClick = { settingsViewModel.setMuted(it) })
    }
}

@Composable
fun BackButton(onBack: () -> Unit, selectedTheme: String) {
    Button(
        modifier = Modifier
            // .fillMaxWidth()
            .padding(top = 16.dp),
        onClick = onBack,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedTheme == "oled") Color.White else SecondaryColor,
            contentColor = if (selectedTheme == "oled") Color.Black else DarkestColor
        )
    ) {
        Text("Back", color = if (selectedTheme == "oled") Color.Black else DarkestColor)
    }
}

@Composable
fun MuteSoundToggleChip(
    selected: Boolean,
    onClick: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(CircleShape)
            .background(
                color = if (selected) {
                    Color(0xFF373B70) // Solid color for selected
                } else {
                    Color(0xFF1C1C1A) // Solid color for unselected
                }
            )
    ) {
        // Use ToggleChip with a switch-like control
        ToggleChip(
            checked = selected,
            onCheckedChange = { onClick(!selected) }, // Toggle the mute state
            label = {
                Text(
                    text = "Mute Sounds",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            toggleControl = {
                // Instead of RadioButton, use Switch for a more fitting behavior
                androidx.wear.compose.material.Switch(
                    checked = selected,
                    onCheckedChange = { onClick(it) } // Toggling switch state
                )
            },
            modifier = Modifier.fillMaxWidth() // Ensure the ToggleChip fills the Box
        )
    }
}


@Composable
fun ThemeToggleChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    ToggleChip(
        checked = selected,
        onCheckedChange = { if (!selected) onClick() }, // Only allow changing if not already selected
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        toggleControl = {
            RadioButton(
                selected = selected,
                onClick = null
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(CircleShape)
            .background(
                color = if (selected) {
                    Color(0xFF373B70) // Solid color for selected
                } else {
                    Color(0xFF1C1C1A) // Solid color for unselected
                }
            )
    )
}