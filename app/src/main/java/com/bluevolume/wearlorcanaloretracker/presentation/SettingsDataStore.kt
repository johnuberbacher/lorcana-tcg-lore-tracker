package com.bluevolume.wearlorcanaloretracker.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create the DataStore
val Context.settingsDataStore by preferencesDataStore(name = "settings")

object SettingsKeys {
    val THEME = stringPreferencesKey("theme")
    val MUTED = booleanPreferencesKey("muted")
    val GAME_MODE = stringPreferencesKey("game_mode")
}

class SettingsDataStore(private val context: Context) {

    // Getters
    val theme: Flow<String> = context.settingsDataStore.data
        .map { preferences -> preferences[SettingsKeys.THEME] ?: "image" }

    val muted: Flow<Boolean> = context.settingsDataStore.data
        .map { preferences -> preferences[SettingsKeys.MUTED] ?: false }

    val gameMode: Flow<String> = context.settingsDataStore.data
        .map { preferences ->
            when (preferences[SettingsKeys.GAME_MODE]) {
                null -> "standard"
                "jafar" -> "jaf" // legacy value from before the mode was renamed
                "ursula" -> "urs" // legacy value from before the mode was renamed
                else -> preferences[SettingsKeys.GAME_MODE]!!
            }
        }

    // Setters
    suspend fun setTheme(theme: String) {
        context.settingsDataStore.edit { settings ->
            settings[SettingsKeys.THEME] = theme
        }
    }

    suspend fun setMuted(muted: Boolean) {
        context.settingsDataStore.edit { settings ->
            settings[SettingsKeys.MUTED] = muted
        }
    }

    suspend fun setGameMode(mode: String) {
        context.settingsDataStore.edit { settings ->
            settings[SettingsKeys.GAME_MODE] = mode
        }
    }
}
