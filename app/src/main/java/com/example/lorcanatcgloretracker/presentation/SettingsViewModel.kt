package com.example.lorcanatcgloretracker.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {
    private val _selectedTheme = MutableStateFlow("image")
    val selectedTheme: StateFlow<String> = _selectedTheme

    fun setTheme(theme: String) {
        _selectedTheme.value = theme
    }
}