package com.example.lorcanatcgloretracker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.InlineSlider
import androidx.wear.compose.material.InlineSliderDefaults
import androidx.wear.compose.material.Text
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(onBack: () -> Unit, settingsViewModel: SettingsViewModel) {
    // State to store the selected theme
    val selectedTheme by settingsViewModel.selectedTheme.collectAsState()
    // State for the slider value
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
            .background(Color.Black),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Ensure the Column takes up full screen
                .focusRequester(focusRequester)
                .focusable()
                .onRotaryScrollEvent { event ->
                    coroutineScope.launch {
                        scrollState.scrollBy(event.verticalScrollPixels)
                    }
                    true // Indicate event was handled
                }
                .padding(16.dp) // Padding for inner content
        ) {
            // Make sure ScalingLazyColumn can scroll by adding a modifier for it to take up available space
            ScalingLazyColumn(
                modifier = Modifier
                    .weight(1f) // Allow it to take the remaining space after the top and bottom elements
                    .fillMaxWidth(), // Ensures it takes up full width
                state = scrollState
            ) {
                item { Text("Settings", style = MaterialTheme.typography.headlineMedium) }

                item {
                    Column(
                        modifier = Modifier
                            .weight(1f) // Allow it to take the remaining space after the top and bottom elements
                            .fillMaxWidth(), // Ensures it takes up full width
                    ) {
                        // Theme style radio buttons
                        Text("Theme Style", style = MaterialTheme.typography.bodyLarge)

                        // Dark theme radio button
                        Row(
                            modifier = Modifier.fillMaxWidth(), // Make it full width
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start // Align items to the left
                        ) {
                            RadioButton(
                                modifier = Modifier.padding(0.dp),
                                selected = selectedTheme == "dark",
                                onClick = { settingsViewModel.setTheme("dark") }
                            )
                            Text("Dark")
                        }

                        // Image theme radio button
                        Row(
                            modifier = Modifier.fillMaxWidth(), // Make it full width
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start // Align items to the left
                        ) {
                            RadioButton(
                                modifier = Modifier.padding(0.dp),
                                selected = selectedTheme == "image",
                                onClick = { settingsViewModel.setTheme("image") }
                            )
                            Text("Image")
                        }


                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Display selected theme
                    Text("Selected Theme: ${selectedTheme ?: "None"}")
                }
                item {
                    // Slider for selecting a value
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
                }
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