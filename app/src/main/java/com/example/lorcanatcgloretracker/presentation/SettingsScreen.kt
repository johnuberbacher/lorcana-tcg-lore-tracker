// SettingsScreen.kt
package com.example.lorcanatcgloretracker.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.example.lorcanatcgloretracker.R
import com.example.lorcanatcgloretracker.presentation.theme.DarkestColor
import com.example.lorcanatcgloretracker.presentation.theme.SecondaryColor
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(onBack: () -> Unit, settingsViewModel: SettingsViewModel) {
    val selectedTheme by settingsViewModel.selectedTheme.collectAsState()
    var value by remember { mutableFloatStateOf(4f) }

    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScalingLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scrollState.scrollToItem(0)
        focusRequester.requestFocus()
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .focusable()
                .onRotaryScrollEvent { event ->
                    coroutineScope.launch {
                        scrollState.scrollBy(event.verticalScrollPixels)
                    }
                    true // Indicate event was handled
                }
                .padding(16.dp)
                .systemBarsPadding()
        ) {
            Scaffold(
                modifier = Modifier,
                positionIndicator = { PositionIndicator(scalingLazyListState = scrollState) }
            ) {
                ScalingLazyColumn(
                    flingBehavior = ScalingLazyColumnDefaults.snapFlingBehavior(state = scrollState),
                    modifier = Modifier
                        .fillMaxWidth() // Ensures it takes up full width
                        .padding(horizontal = 0.dp), // Ensure padding on sides
                    state = scrollState
                ) {
                    item {
                        Text(
                            "Settings",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp) // Top padding for better spacing
                        ) {
                            // Theme style radio buttons
                            Text(
                                "Theme Style",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(bottom = 6.dp) // Add some padding below the title
                            )

                            // Image theme radio button
                            ThemeRadioButton(
                                label = "Image",
                                selected = selectedTheme == "image",
                                onClick = { settingsViewModel.setTheme("image") },
                                selectedTheme = selectedTheme
                            )

                            // Dark theme radio button
                            ThemeRadioButton(
                                label = "Dark",
                                selected = selectedTheme == "dark",
                                onClick = { settingsViewModel.setTheme("dark") },
                                selectedTheme = selectedTheme
                            )

                            // OLED theme radio button
                            ThemeRadioButton(
                                label = "OLED",
                                selected = selectedTheme == "old",
                                onClick = { settingsViewModel.setTheme("oled") },
                                selectedTheme = selectedTheme
                            )
                        }
                    }

                    item {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTheme == "oled") Color.White else SecondaryColor,
                                contentColor = if (selectedTheme == "oled") Color.Black else DarkestColor
                            )
                        ) {
                            Text("Back")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeRadioButton(label: String, selected: Boolean, onClick: () -> Unit, selectedTheme: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp) // Reduced vertical padding
            .clip(CircleShape) // Rounded background
            .background(
                when (selectedTheme) {
                    "dark" -> Color.White.copy(alpha = 0.1f)
                    "oled" -> Color.White.copy(alpha = 0.0f)
                    else -> Color.Black.copy(alpha = 0.33f)
                }
            )
            .padding(8.dp), // Padding inside the background
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(
            label,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
