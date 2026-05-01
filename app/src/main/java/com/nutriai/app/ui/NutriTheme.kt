package com.nutriai.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightScheme: ColorScheme = lightColorScheme(
    primary = Color(0xFF16695C),
    onPrimary = Color.White,
    secondary = Color(0xFF8B5E34),
    tertiary = Color(0xFFB43E4F),
    background = Color(0xFFF7F8F4),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE4E9DF),
    onSurface = Color(0xFF1D2420)
)

private val DarkScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFF7FD6C8),
    secondary = Color(0xFFD8B28A),
    tertiary = Color(0xFFFFA1B0),
    background = Color(0xFF111512),
    surface = Color(0xFF1B211D),
    surfaceVariant = Color(0xFF313A34)
)

@Composable
fun NutriTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkScheme else LightScheme,
        content = content
    )
}
