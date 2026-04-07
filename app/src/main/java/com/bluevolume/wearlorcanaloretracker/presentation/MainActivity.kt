// MainActivity.kt
package com.bluevolume.wearlorcanaloretracker.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bluevolume.wearlorcanaloretracker.R
import com.bluevolume.wearlorcanaloretracker.presentation.theme.LorcanaTCGLoreTrackerTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController

class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            LorcanaTCGLoreTrackerTheme {
                WearApp(settingsViewModel)
            }
        }
    }

    fun takeScreenshot() {
        captureScreenshotToGallery(this) { success ->
            Toast.makeText(
                this,
                if (success) getString(R.string.screenshot_saved) else getString(R.string.screenshot_failed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun WearApp(settingsViewModel: SettingsViewModel) {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(navController = navController, startDestination = "main") {
        composable("main") {
            val gameMode by settingsViewModel.gameMode.collectAsState()
            val onOpenSettings = { navController.navigate("settings") }
            val onOpenGameover = { winner: String -> navController.navigate("gameover/$winner") }

            if (gameMode == "standard") {
                MainScreen(
                    onOpenSettings = onOpenSettings,
                    onOpenGameover = onOpenGameover,
                    settingsViewModel = settingsViewModel
                )
            } else {
                VillainMainScreen(
                    onOpenSettings = onOpenSettings,
                    onOpenGameover = onOpenGameover,
                    settingsViewModel = settingsViewModel
                )
            }
        }
        composable("settings") {
            SettingsScreen(settingsViewModel = settingsViewModel)
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
