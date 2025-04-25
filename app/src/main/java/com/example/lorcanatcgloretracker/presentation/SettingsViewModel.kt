package com.example.lorcanatcgloretracker.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {

    // Theme state (can be "dark", "light", "image", or null)
    private val _selectedTheme = MutableStateFlow<String?>(null)
    val selectedTheme: StateFlow<String?> = _selectedTheme

    fun setTheme(theme: String) {
        _selectedTheme.value = theme
    }
}