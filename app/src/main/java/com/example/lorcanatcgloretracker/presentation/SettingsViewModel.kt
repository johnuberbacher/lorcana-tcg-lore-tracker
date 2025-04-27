package com.example.lorcanatcgloretracker.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lorcanatcgloretracker.data.SettingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsDataStore = SettingsDataStore(application)

    private val _selectedTheme = MutableStateFlow("image")
    val selectedTheme = _selectedTheme.asStateFlow()

    private val _muted = MutableStateFlow(false)
    val muted = _muted.asStateFlow()

    init {
        settingsDataStore.theme
            .onEach { _selectedTheme.value = it }
            .launchIn(viewModelScope)

        settingsDataStore.muted
            .onEach { _muted.value = it }
            .launchIn(viewModelScope)
    }

    fun setTheme(theme: String) {
        viewModelScope.launch {
            settingsDataStore.setTheme(theme)
        }
    }

    fun setMuted(mute: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setMuted(mute)
        }
    }
}
