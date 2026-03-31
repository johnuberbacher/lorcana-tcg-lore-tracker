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

    // Game state that persists across navigation
    private val _leftCount = MutableStateFlow(0)
    val leftCount = _leftCount.asStateFlow()

    private val _rightCount = MutableStateFlow(0)
    val rightCount = _rightCount.asStateFlow()

    private val _maxLoreCount = MutableStateFlow(20)
    val maxLoreCount = _maxLoreCount.asStateFlow()

    private val _loreIndex = MutableStateFlow(0)
    val loreIndex = _loreIndex.asStateFlow()

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver = _isGameOver.asStateFlow()

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

    fun incrementLeft() { _leftCount.value++ }
    fun decrementLeft() { _leftCount.value-- }
    fun incrementRight() { _rightCount.value++ }
    fun decrementRight() { _rightCount.value-- }
    fun setGameOver(value: Boolean) { _isGameOver.value = value }
    fun resetGame() {
        _leftCount.value = 0
        _rightCount.value = 0
        _maxLoreCount.value = 20
        _loreIndex.value = 0
        _isGameOver.value = false
    }

    private val loreValues = listOf(20, 25, 10, 15)
    fun cycleMaxLoreCount() {
        val nextIndex = (_loreIndex.value + 1) % loreValues.size
        _loreIndex.value = nextIndex
        _maxLoreCount.value = loreValues[nextIndex]
    }
}
