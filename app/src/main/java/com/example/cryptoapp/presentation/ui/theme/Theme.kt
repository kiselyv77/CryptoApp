package com.example.cryptoapp.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = LightBlue,
    background = DarkBlue,
    onPrimary = LightBlue,
    onBackground = TextWhite
)

private val LightColorPalette = lightColors(
    primary = LightBlue,
    background = DarkBlue,
    onPrimary = LightBlue,
    onBackground = TextWhite
)

@Composable
fun CryptoAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}