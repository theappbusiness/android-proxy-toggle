package com.kinandcarta.create.proxytoggle.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = lightColorScheme(
    primary = VividPurple,
    surface = LightGray,
    surfaceTint = Color.Transparent
)

private val DarkColorPalette = darkColorScheme(
    primary = Periwinkle,
    surface = Dark,
    surfaceTint = Color.Transparent
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
        colorScheme = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
