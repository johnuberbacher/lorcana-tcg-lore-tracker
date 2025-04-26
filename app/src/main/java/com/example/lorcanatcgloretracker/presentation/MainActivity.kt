package com.example.lorcanatcgloretracker.presentation

import android.Manifest
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.io.IOException

class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    // Permissions request launcher
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                // Permission granted, proceed with saving the screenshot
                takeScreenshot()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                WearApp(settingsViewModel)
            }
        }
    }


    fun takeScreenshot() {
        // Check for Android version (API 29+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // On Android 10+ (API 29+), check if we have permission for media storage
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                // Request the permission
                requestPermissionsLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                // If permission is granted, capture and save the screenshot
                captureAndSaveScreenshot()
            }
        } else {
            // For older versions (below Android 10), we can use WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                // Request the permission
                requestPermissionsLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                // If permission is granted, capture and save the screenshot
                captureAndSaveScreenshot()
            }
        }
    }

    private fun captureAndSaveScreenshot() {
        // Check for Android version (API 29+)
        val rootView = window.decorView.rootView
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // API 29 and above use drawToBitmap (modern approach)
            rootView.drawToBitmap()
        } else {
            // For lower versions, fallback to the older approach
            val width = rootView.width
            val height = rootView.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            rootView.draw(Canvas(bitmap))
            bitmap
        }

        // Save to external storage or MediaStore (depending on Android version)
        try {
            val contentValues = ContentValues().apply {
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    "screenshot_${System.currentTimeMillis()}.png"
                )
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                // For Android 10 and above, save to a folder in the Pictures directory
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/LorcanATCG")
            }

            val uri =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            val outputStream = contentResolver.openOutputStream(uri!!)

            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            outputStream?.close()

            Toast.makeText(this, "Screenshot saved!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error saving screenshot", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun WearApp(settingsViewModel: SettingsViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onOpenSettings = { navController.navigate("settings") },
                onOpenGameover = { winner ->
                    navController.navigate("gameover/$winner")
                },
                settingsViewModel = settingsViewModel
            )
        }
        composable("settings") {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                settingsViewModel = settingsViewModel
            )
        }
        composable("gameover/{winner}") { backStackEntry ->
            val winner = backStackEntry.arguments?.getString("winner") ?: "Unknown"
            GameoverScreen(
                onBack = { navController.popBackStack() },
                takeScreenshot = { (navController.context as MainActivity).takeScreenshot() },
                settingsViewModel = settingsViewModel,
                winner = winner
            )
        }
    }
}
