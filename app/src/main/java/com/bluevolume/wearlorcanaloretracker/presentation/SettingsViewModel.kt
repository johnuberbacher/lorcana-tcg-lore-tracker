package com.bluevolume.wearlorcanaloretracker.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bluevolume.wearlorcanaloretracker.data.SettingsDataStore
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

    // Game mode: "standard", "urs" (Deep Trouble), or "jaf" (Palace Heist)
    private val _gameMode = MutableStateFlow("standard")
    val gameMode = _gameMode.asStateFlow()

    // Villain quest state (only used when gameMode != "standard")
    private val _villainCount = MutableStateFlow(0)
    val villainCount = _villainCount.asStateFlow()

    // One-way ratchet: 0 = base draw, 1/2 = bonus draw steps unlocked at 10/30 villain lore
    private val _ursDrawStage = MutableStateFlow(0)
    val ursDrawStage = _ursDrawStage.asStateFlow()

    // "jaf" or "players" - who currently holds the Reforged Crown
    private val _crownHolder = MutableStateFlow("jaf")
    val crownHolder = _crownHolder.asStateFlow()

    init {
        settingsDataStore.theme
            .onEach { _selectedTheme.value = it }
            .launchIn(viewModelScope)

        settingsDataStore.muted
            .onEach { _muted.value = it }
            .launchIn(viewModelScope)

        settingsDataStore.gameMode
            .onEach { _gameMode.value = it }
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

    fun setGameMode(mode: String) {
        viewModelScope.launch {
            settingsDataStore.setGameMode(mode)
        }
        resetGame()
    }

    fun incrementLeft() { _leftCount.value++ }
    fun decrementLeft() { _leftCount.value-- }
    fun incrementRight() { _rightCount.value++ }
    fun decrementRight() { _rightCount.value-- }
    fun setGameOver(value: Boolean) { _isGameOver.value = value }

    fun incrementVillain() {
        _villainCount.value++
        if (_ursDrawStage.value < 1 && _villainCount.value >= 10) _ursDrawStage.value = 1
        if (_ursDrawStage.value < 2 && _villainCount.value >= 30) _ursDrawStage.value = 2
    }
    fun decrementVillain() { _villainCount.value-- }
    fun toggleCrownHolder() {
        _crownHolder.value = if (_crownHolder.value == "jaf") "players" else "jaf"
    }

    fun resetGame() {
        _leftCount.value = 0
        _rightCount.value = 0
        _maxLoreCount.value = 20
        _loreIndex.value = 0
        _isGameOver.value = false
        _villainCount.value = 0
        _ursDrawStage.value = 0
        _crownHolder.value = "jaf"
    }

    private val loreValues = listOf(20, 25, 10, 15)
    fun cycleMaxLoreCount() {
        val nextIndex = (_loreIndex.value + 1) % loreValues.size
        _loreIndex.value = nextIndex
        _maxLoreCount.value = loreValues[nextIndex]
    }
}
