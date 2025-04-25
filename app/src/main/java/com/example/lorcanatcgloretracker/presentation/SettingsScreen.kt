package com.example.lorcanatcgloretracker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(onBack: () -> Unit, settingsViewModel: SettingsViewModel) {
    val selectedTheme by settingsViewModel.selectedTheme.collectAsState()
    var value by remember { mutableFloatStateOf(4f) }

    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScalingLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Ensure the Column takes up full screen
                .focusRequester(focusRequester)
                .focusable()
                .onRotaryScrollEvent { event ->
                    coroutineScope.launch {
                        scrollState.scrollBy(event.verticalScrollPixels)
                        scrollState.animateScrollBy(0f)
                    }
                    true // Indicate event was handled
                }
                .padding(0.dp) // Padding for inner content
        ) {
            Scaffold(
                modifier = Modifier,
                positionIndicator = { PositionIndicator(scalingLazyListState = scrollState) }
            ) {
                // Make sure ScalingLazyColumn can scroll by adding a modifier for it to take up available space
                ScalingLazyColumn(
                    flingBehavior = ScalingLazyColumnDefaults.snapFlingBehavior(state = scrollState),
                    modifier = Modifier
                        // .weight(1f) // Allow it to take the remaining space after the top and bottom elements
                        .fillMaxWidth(), // Ensures it takes up full width
                    state = scrollState,
                    // anchorType = ScalingLazyListAnchorType.ItemStart
                ) {
                    item { Text("Settings", style = MaterialTheme.typography.headlineSmall) }

                    item {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                        ) {
                            // Theme style radio buttons
                            Text("Theme Style", style = MaterialTheme.typography.bodyMedium)

                            // Image theme radio button
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { settingsViewModel.setTheme("image") }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                RadioButton(
                                    selected = selectedTheme == "image",
                                    onClick = null
                                )
                                Text("Image", modifier = Modifier.padding(start = 8.dp))
                            }

                            // Dark theme radio button
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { settingsViewModel.setTheme("dark") } // <-- make whole row clickable
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                RadioButton(
                                    selected = selectedTheme == "dark",
                                    onClick = null // handled by Row's clickable
                                )
                                Text("Dark", modifier = Modifier.padding(start = 8.dp))
                            }

                            // OLED theme radio button
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { settingsViewModel.setTheme("oled") }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                RadioButton(
                                    selected = selectedTheme == "oled",
                                    onClick = null
                                )
                                Text("OLED", modifier = Modifier.padding(start = 8.dp))
                            }


                        }
                    }
                    // Slider for selecting a value
                    /* item {
                    InlineSlider(
                        value = value,
                        onValueChange = { value = it },
                        increaseIcon = {
                            Icon(
                                InlineSliderDefaults.Increase,
                                contentDescription = "Increase"
                            )
                        },
                        decreaseIcon = {
                            Icon(
                                InlineSliderDefaults.Decrease,
                                contentDescription = "Decrease"
                            )
                        },
                        valueRange = 3f..6f,
                        steps = 5,
                        segmented = true
                    )
                } */
                    item {
                        Button(
                            modifier = Modifier.fillMaxWidth(), // Make the button full width
                            onClick = onBack
                        ) {
                            Text("Back")
                        }
                    }
                }
            }
        }
    }
}