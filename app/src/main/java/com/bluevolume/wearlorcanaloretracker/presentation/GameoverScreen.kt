// GameoverScreen.kt
package com.bluevolume.wearlorcanaloretracker.presentation

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.media.SoundPool
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.PixelCopy
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.bluevolume.wearlorcanaloretracker.R
import com.bluevolume.wearlorcanaloretracker.presentation.theme.DarkestColor
import com.bluevolume.wearlorcanaloretracker.presentation.theme.MyFontFamily
import com.bluevolume.wearlorcanaloretracker.presentation.theme.SecondaryColor

fun captureScreenshotToGallery(activity: Activity, onResult: (Boolean) -> Unit) {
    val window = activity.window
    val view = window.decorView.rootView
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    try {
        PixelCopy.request(
            window,
            bitmap,
            { result ->
                if (result == PixelCopy.SUCCESS) {
                    saveBitmap(activity, bitmap)
                    onResult(true)
                } else {
                    onResult(false)
                }
            },
            Handler(Looper.getMainLooper())
        )
    } catch (e: Exception) {
        e.printStackTrace()
        onResult(false)
    }
}

fun saveBitmap(activity: Activity, bitmap: Bitmap): Uri? {
    val filename = "screenshot_${System.currentTimeMillis()}.png"
    val resolver = activity.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Screenshots")
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    imageUri?.let { uri ->
        resolver.openOutputStream(uri)?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(uri, contentValues, null, null)
    } ?: run {
        Toast.makeText(activity, "Failed to save screenshot", Toast.LENGTH_SHORT).show()
    }

    return imageUri
}

@Composable
fun GameoverScreen(
    onBack: () -> Unit,
    takeScreenshot: () -> Unit,
    settingsViewModel: SettingsViewModel,
    winner: String // Accept the winner string
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val soundPool = remember { SoundPool.Builder().setMaxStreams(2).build() }
    val muted by settingsViewModel.muted.collectAsState()

    val selectedTheme by settingsViewModel.selectedTheme.collectAsState()
    val gameMode by settingsViewModel.gameMode.collectAsState()
    val villainWon = gameMode != "standard" && winner != "You Win!"

    val winSoundRes = if (villainWon) R.raw.villain_win else R.raw.game_complete
    val soundGameWon = remember(winSoundRes) { soundPool.load(activity, winSoundRes, 1) }

    DisposableEffect(Unit) {
        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (!muted && status == 0) {
                soundPool.play(soundGameWon, 1f, 1f, 1, 0, 1f)
            } else {
                // Don't play nuthin
            }
        }
        onDispose {
            soundPool.release()
        }
    }

    rememberNavController()

    val backgroundRes = when {
        gameMode == "jaf" && villainWon -> R.drawable.image_background_jaf_win
        gameMode == "jaf" -> R.drawable.image_background_jaf
        gameMode == "urs" && villainWon -> R.drawable.image_background_urs_win
        gameMode == "urs" -> R.drawable.image_background_urs
        else -> R.drawable.image_background
    }
    val villainTint = if (gameMode == "jaf") Color(0xFF8B0000) else Color(0xFF4B0082)

    Box(modifier = Modifier.fillMaxSize()) {
        if (selectedTheme == "image") {
            Image(
                painter = painterResource(id = backgroundRes),
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
            if (selectedTheme == "dark" && gameMode != "standard") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(villainTint.copy(alpha = 0.25f))
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                text = winner,
                textAlign = TextAlign.Center,
                fontFamily = MyFontFamily,
                color = if (selectedTheme == "oled") Color.White else SecondaryColor,
                fontSize = 24.sp,
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
                // Save button
                Button(
                    onClick = {
                        activity?.let { act ->
                            captureScreenshotToGallery(act) { success ->
                                Toast.makeText(
                                    context,
                                    if (success) {
                                        context.getString(R.string.screenshot_saved)
                                    } else {
                                        context.getString(R.string.screenshot_failed)
                                    },
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
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
