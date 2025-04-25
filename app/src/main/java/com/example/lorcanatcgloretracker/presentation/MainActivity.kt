//MainActivity.kt

package com.example.lorcanatcgloretracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                WearApp(settingsViewModel)
            }
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
                settingsViewModel = settingsViewModel
            )
        }
        composable("settings") {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                settingsViewModel = settingsViewModel
            )
        }
    }
}

fun onDispose(function: () -> Unit) {

}
