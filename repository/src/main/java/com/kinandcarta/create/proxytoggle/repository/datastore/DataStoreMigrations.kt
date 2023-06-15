package com.kinandcarta.create.proxytoggle.repository.datastore

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import com.kinandcarta.create.proxytoggle.datastore.AppData
import com.kinandcarta.create.proxytoggle.datastore.PastProxy
import com.kinandcarta.create.proxytoggle.datastore.UserPreferences
import com.kinandcarta.create.proxytoggle.repository.proxymapper.ProxyMapper

private const val SHARED_PREF_NAME = "AppSettings"
private const val PREF_PROXY = "proxy"
private const val PREF_THEME = "theme"

fun appDataMigration(context: Context, proxyMapper: ProxyMapper) = SharedPreferencesMigration(
    context = context,
    sharedPreferencesName = SHARED_PREF_NAME,
    keysToMigrate = setOf(PREF_PROXY)
) { sharedPrefsView: SharedPreferencesView, currentData: AppData ->
    val noProxiesInDataStore = currentData.pastProxiesList.isEmpty()
    val proxyInPrefs = sharedPrefsView.getString(PREF_PROXY)
    val builder = currentData.toBuilder()
    if (noProxiesInDataStore && proxyInPrefs != null) {
        val savedProxy = proxyMapper.from(proxyInPrefs)
        builder.addPastProxies(
            PastProxy.newBuilder()
                .setAddress(savedProxy.address)
                .setPort(savedProxy.port)
                .setTimestamp(0)
                .build()
        )
    }
    builder.build()
}

fun userPreferencesMigration(context: Context) = SharedPreferencesMigration(
    context = context,
    sharedPreferencesName = SHARED_PREF_NAME,
    keysToMigrate = setOf(PREF_THEME)
) { sharedPrefsView: SharedPreferencesView, currentData: UserPreferences ->
    val noThemeInDataStore = currentData.themeMode == UserPreferences.ThemeMode.UNSPECIFIED
    val builder = currentData.toBuilder()
    if (noThemeInDataStore) {
        val themeInPrefs = sharedPrefsView.getInt(
            PREF_THEME,
            AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
        )
        if (themeInPrefs != AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
            val themeMode = when (themeInPrefs) {
                AppCompatDelegate.MODE_NIGHT_NO -> UserPreferences.ThemeMode.LIGHT
                else -> UserPreferences.ThemeMode.DARK
            }
            builder.themeMode = themeMode
        } else {
            builder.themeMode = UserPreferences.ThemeMode.LIGHT
        }
    }
    builder.build()
}
