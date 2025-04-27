// GameoverScreen.kt
package com.example.lorcanatcgloretracker.presentation

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Rect
import android.media.MediaScannerConnection
import android.media.SoundPool
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.Window
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
import com.example.lorcanatcgloretracker.R
import com.example.lorcanatcgloretracker.presentation.theme.DarkestColor
import com.example.lorcanatcgloretracker.presentation.theme.MyFontFamily
import com.example.lorcanatcgloretracker.presentation.theme.SecondaryColor

fun takeScreenshot(activity: Activity, onResult: (Boolean) -> Unit) {
    val window: Window = activity.window
    val view = window.decorView.rootView
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        try {
            PixelCopy.request(
                window,
                Rect(location[0], location[1], location[0] + view.width, location[1] + view.height),
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
    } else {
        view.isDrawingCacheEnabled = true
        val cache = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        saveBitmap(activity, cache)
        onResult(true)
    }
}

fun saveBitmap(activity: Activity, bitmap: Bitmap) {
    val filename = "screenshot_${System.currentTimeMillis()}.png"
    val resolver = activity.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Screenshots") // Scoped storage
        } else {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Screenshots") // Legacy
        }
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    imageUri?.let { uri ->
        val fos = resolver.openOutputStream(uri)
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(uri, contentValues, null, null)

        // Notify media scanner
        MediaScannerConnection.scanFile(
            activity,
            arrayOf(uri.toString()),
            arrayOf("image/png"),
            null
        )
    } ?: run {
        // Fallback if uri is null
        Toast.makeText(activity, "Failed to save screenshot", Toast.LENGTH_SHORT).show()
    }
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

    val soundGameWon = remember { soundPool.load(activity, R.raw.game_complete, 1) }

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
                // Right Button: Save/Download Icon
                /* Button(
                    onClick = {
                        activity?.let { act ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                // Android Q+ don't need WRITE_EXTERNAL_STORAGE permission for saving in MediaStore
                                takeScreenshot(act) { success ->
                                    if (success) {
                                        Toast.makeText(
                                            context,
                                            "Screenshot saved",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Failed to save screenshot",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                // For devices below Android Q, ensure WRITE_EXTERNAL_STORAGE permission is checked
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    ActivityCompat.requestPermissions(
                                        act,
                                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        1000
                                    )
                                    Toast.makeText(
                                        context,
                                        "Please grant storage permission and try again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@let
                                }
                                // Proceed with screenshot
                                takeScreenshot(act) { success ->
                                    if (success) {
                                        Toast.makeText(
                                            context,
                                            "Screenshot saved",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Failed to save screenshot",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    }
                )
                {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save Icon",
                        tint = if (selectedTheme == "oled") Color.Black else DarkestColor,
                        modifier = Modifier.size(24.dp)
                    )
                } */
            }
        }
    }
}
