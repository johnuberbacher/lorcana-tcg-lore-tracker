// Colors.kt
package com.example.lorcanatcgloretracker.presentation.theme

import androidx.wear.compose.material.Colors
import androidx.compose.ui.graphics.Color

val PrimaryColor = Color(0xFF373B70)
val SecondaryColor = Color(0xFFD3BA84)
val AccentColor = Color(0xFFE3CAA8)
val DarkestColor = Color(0xFF1C1C1A)

val OnPrimary = Color.White
val OnSecondary = Color.Black
val OnAccent = Color.Black
val OnDarkest = Color.White

val MyColors = Colors(
    primary = PrimaryColor,
    primaryVariant = PrimaryColor,
    secondary = SecondaryColor,
    secondaryVariant = SecondaryColor,
    background = DarkestColor,
    surface = AccentColor,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary,
    onBackground = OnDarkest,
    onSurface = OnAccent,
    error = Color.Red,
    onError = Color.White,
)