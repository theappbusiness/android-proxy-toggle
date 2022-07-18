package com.kinandcarta.create.proxytoggle.android

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate
import com.kinandcarta.create.proxytoggle.core.android.ThemeSwitcher
import com.kinandcarta.create.proxytoggle.core.settings.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeSwitcherImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val appSettings: AppSettings
) : ThemeSwitcher {

    init {
        val mode =
            when {
                appSettings.themeMode.isNightMode() || appSettings.themeMode.isLightMode() -> {
                    appSettings.themeMode
                }
                context.isSetToDarkMode() -> {
                    AppCompatDelegate.MODE_NIGHT_YES
                }
                else -> {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            }
        appSettings.themeMode = mode
    }

    override fun toggleTheme() {
        if (appSettings.themeMode.isNightMode()) {
            appSettings.themeMode = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            appSettings.themeMode = AppCompatDelegate.MODE_NIGHT_YES
        }
    }

    override fun isNightMode() = appSettings.themeMode.isNightMode()

    private fun Int.isNightMode() = this == AppCompatDelegate.MODE_NIGHT_YES
    private fun Int.isLightMode() = this == AppCompatDelegate.MODE_NIGHT_NO
    private fun Context.isSetToDarkMode() =
        this.resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}
