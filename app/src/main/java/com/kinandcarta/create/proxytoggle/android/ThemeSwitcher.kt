package com.kinandcarta.create.proxytoggle.android

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate
import com.kinandcarta.create.proxytoggle.settings.AppSettings
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ThemeSwitcher @Inject constructor(
    @ActivityContext context: Context,
    private val appSettings: AppSettings
) {

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
        setTheme(mode)
    }

    fun toggleTheme() {
        if (appSettings.themeMode.isNightMode()) {
            setTheme(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            setTheme(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun setTheme(mode: Int) {
        appSettings.themeMode = mode
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun Int.isNightMode() = this == AppCompatDelegate.MODE_NIGHT_YES
    private fun Int.isLightMode() = this == AppCompatDelegate.MODE_NIGHT_NO
    private fun Context.isSetToDarkMode() =
        this.resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}
