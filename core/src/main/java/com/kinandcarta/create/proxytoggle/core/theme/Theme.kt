package com.kinandcarta.create.proxytoggle.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = lightColors(
    primary = VividPurple,
    surface = LightGray
)

private val DarkColorPalette = darkColors(
    primary = Periwinkle,
    surface = Dark
)

@Composable
fun ProxyToggleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val statusBarColor = if (darkTheme) Dark else LightGray
    rememberSystemUiController().setSystemBarsColor(statusBarColor)

    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
