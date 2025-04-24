package com.example.lorcanatcgloretracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lorcanatcgloretracker.presentation.theme.MyFontFamily

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                WearApp()
            }
        }
    }
}

@Composable
fun WearApp() {
    var maxLoreCount by remember { mutableIntStateOf(20) }
    var leftCount by remember { mutableIntStateOf(0) }
    var rightCount by remember { mutableIntStateOf(0) }

    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Left Half
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(colors.primary)
                    .clickable { leftCount++ },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$leftCount",
                    fontFamily = MyFontFamily, color = colors.onPrimary, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }

            // Right Half
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(colors.primary)
                    .clickable { rightCount++ },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$rightCount",
                    fontFamily = MyFontFamily, color = colors.onPrimary, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.1f))
                    .clickable(
                        onClick = { /* Do nothing for now */ },
                        role = null // You can add Role.Button if this is a button
                    )
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space = 3.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 3.dp)) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(bottom = 1.dp)) {
                        Text(text = "$rightCount",
                            textAlign = TextAlign.Center,
                            fontFamily = MyFontFamily, color = colors.onPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Settings Icon - Bottom Center
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.1f))
                    .clickable(
                        onClick = { /* Do nothing for now */ },
                        role = null // You can add Role.Button if this is a button
                    )
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewWearApp() {
    WearApp()
}
