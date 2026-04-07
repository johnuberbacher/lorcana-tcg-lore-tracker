// SettingsScreen.kt
package com.bluevolume.wearlorcanaloretracker.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.RadioButtonDefaults
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.SwitchButton
import androidx.wear.compose.material3.SwitchButtonDefaults
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    val selectedTheme by settingsViewModel.selectedTheme.collectAsState()
    val muted by settingsViewModel.muted.collectAsState()
    val gameMode by settingsViewModel.gameMode.collectAsState()

    val listState = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    ScreenScaffold(
        scrollState = listState,
        modifier = Modifier.fillMaxSize(),
        timeText = {}
    ) { contentPadding ->
        TransformingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding
        ) {
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec)
                ) {
                    Text("Game Mode")
                }
            }
            item {
                RadioButton(
                    selected = gameMode == "standard",
                    onSelect = { settingsViewModel.setGameMode("standard") },
                    label = { Text("Standard") },
                    colors = RadioButtonDefaults.radioButtonColors(
                        selectedContainerColor = Color(0xFF373B70),
                        unselectedContainerColor = Color(0xFF1C1C1A),
                    ),
                    transformation = SurfaceTransformation(spec),
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, spec)
                )
            }
            item {
                RadioButton(
                    selected = gameMode == "urs",
                    onSelect = { settingsViewModel.setGameMode("urs") },
                    label = { Text("Deep Trouble") },
                    colors = RadioButtonDefaults.radioButtonColors(
                        selectedContainerColor = Color(0xFF373B70),
                        unselectedContainerColor = Color(0xFF1C1C1A),
                    ),
                    transformation = SurfaceTransformation(spec),
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, spec)
                )
            }
            item {
                RadioButton(
                    selected = gameMode == "jaf",
                    onSelect = { settingsViewModel.setGameMode("jaf") },
                    label = { Text("Palace Heist") },
                    colors = RadioButtonDefaults.radioButtonColors(
                        selectedContainerColor = Color(0xFF373B70),
                        unselectedContainerColor = Color(0xFF1C1C1A),
                    ),
                    transformation = SurfaceTransformation(spec),
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, spec)
                )
            }
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec)
                ) {
                    Text("Theme Style")
                }
            }
            item {
                RadioButton(
                    selected = selectedTheme == "image",
                    onSelect = { settingsViewModel.setTheme("image") },
                    label = { Text("Image") },
                    colors = RadioButtonDefaults.radioButtonColors(
                        selectedContainerColor = Color(0xFF373B70),
                        unselectedContainerColor = Color(0xFF1C1C1A),
                    ),
                    transformation = SurfaceTransformation(spec),
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, spec)
                )
            }
            item {
                RadioButton(
                    selected = selectedTheme == "dark",
                    onSelect = { settingsViewModel.setTheme("dark") },
                    label = { Text("Dark") },
                    colors = RadioButtonDefaults.radioButtonColors(
                        selectedContainerColor = Color(0xFF373B70),
                        unselectedContainerColor = Color(0xFF1C1C1A),
                    ),
                    transformation = SurfaceTransformation(spec),
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, spec)
                )
            }
            item {
                RadioButton(
                    selected = selectedTheme == "oled",
                    onSelect = { settingsViewModel.setTheme("oled") },
                    label = { Text("OLED") },
                    colors = RadioButtonDefaults.radioButtonColors(
                        selectedContainerColor = Color(0xFF373B70),
                        unselectedContainerColor = Color(0xFF1C1C1A),
                    ),
                    transformation = SurfaceTransformation(spec),
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, spec)
                )
            }
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec)
                ) {
                    Text("Sound")
                }
            }
            item {
                SwitchButton(
                    checked = muted,
                    onCheckedChange = { settingsViewModel.setMuted(it) },
                    label = { Text("Mute Sounds") },
                    colors = SwitchButtonDefaults.switchButtonColors(
                        checkedContainerColor = Color(0xFF373B70),
                        uncheckedContainerColor = Color(0xFF1C1C1A),
                    ),
                    transformation = SurfaceTransformation(spec),
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, spec)
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

