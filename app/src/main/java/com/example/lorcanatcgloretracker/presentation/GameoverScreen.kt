package com.example.lorcanatcgloretracker.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.lorcanatcgloretracker.R
import com.example.lorcanatcgloretracker.presentation.theme.DarkestColor
import com.example.lorcanatcgloretracker.presentation.theme.MyFontFamily
import com.example.lorcanatcgloretracker.presentation.theme.SecondaryColor

@Composable
fun GameoverScreen(
    onBack: () -> Unit,
    takeScreenshot: () -> Unit,
    settingsViewModel: SettingsViewModel,
    winner: String // Accept the winner string
) {
    val navController = rememberNavController()
    val selectedTheme by settingsViewModel.selectedTheme.collectAsState()

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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                text = winner,
                textAlign = TextAlign.Center,
                fontFamily = MyFontFamily,
                color = if (selectedTheme == "oled") Color.White else SecondaryColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Row for the buttons side by side
            Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp), // <-- add space between buttons
                modifier = Modifier.padding(top = 16.dp)
            ) {
                // Left Button: Back/Restart Icon
                Button(
                    onClick = onBack,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTheme == "oled") Color.White else SecondaryColor,
                        contentColor = if (selectedTheme == "oled") Color.Black else DarkestColor
                    ),
                    modifier = Modifier.size(40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Back Icon",
                        tint = if (selectedTheme == "oled") Color.Black else DarkestColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Right Button: Save/Download Icon
                Button(
                    onClick = {
                        takeScreenshot()
                        Toast.makeText(
                            navController.context,
                            "Screenshot saved",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTheme == "oled") Color.White else SecondaryColor,
                        contentColor = if (selectedTheme == "oled") Color.Black else DarkestColor
                    ),
                    modifier = Modifier.size(40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save Icon",
                        tint = if (selectedTheme == "oled") Color.Black else DarkestColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
