package com.example.lorcanatcgloretracker.data

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
}

class SettingsDataStore(private val context: Context) {

    // Getters
    val theme: Flow<String> = context.settingsDataStore.data
        .map { preferences -> preferences[SettingsKeys.THEME] ?: "image" }

    val muted: Flow<Boolean> = context.settingsDataStore.data
        .map { preferences -> preferences[SettingsKeys.MUTED] ?: false }

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
}
