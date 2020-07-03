package com.kinandcarta.create.proxytoggle.android

import android.content.Context
import androidx.core.content.edit
import com.kinandcarta.create.proxytoggle.model.Proxy
import com.kinandcarta.create.proxytoggle.model.ProxyMapper
import com.kinandcarta.create.proxytoggle.settings.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefsAppSettings @Inject constructor(
    @ApplicationContext private val context: Context,
    private val proxyMapper: ProxyMapper
) : AppSettings {

    companion object {
        private const val SHARED_PREF_NAME = "AppSettings"
        private const val PREF_PROXY = "proxy"
    }

    private val prefs by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    override var lastUsedProxy: Proxy
        get() = proxyMapper.from(prefs.getString(PREF_PROXY, null))
        set(value) = prefs.edit { putString(PREF_PROXY, value.toString()) }
}
