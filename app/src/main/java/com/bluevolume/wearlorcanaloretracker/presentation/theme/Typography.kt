// Typography.kt
package com.bluevolume.wearlorcanaloretracker.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bluevolume.wearlorcanaloretracker.R

val MyFontFamily = FontFamily(
    Font(R.font.bystander_semibold, FontWeight.Normal),
    Font(R.font.bystander_semibold, FontWeight.Bold)
)

val MyTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = MyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    titleLarge = TextStyle(
        fontFamily = MyFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
)
