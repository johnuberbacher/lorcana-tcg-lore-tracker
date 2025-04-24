// Typography.kt
package com.example.lorcanatcgloretracker.presentation.theme
import com.example.lorcanatcgloretracker.R

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.wear.compose.material.Typography
import androidx.compose.ui.unit.sp

val MyFontFamily = FontFamily(
    Font(R.font.bystander_semibold, FontWeight.Normal),
    Font(R.font.bystander_semibold, FontWeight.Bold)
)

val MyTypography = Typography(
    body1 = TextStyle(
        fontFamily = MyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    title1 = TextStyle(
        fontFamily = MyFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
)
