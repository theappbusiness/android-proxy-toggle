package com.kinandcarta.create.proxytoggle.android

import android.content.Context
import android.provider.Settings
import com.kinandcarta.create.proxytoggle.core.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.core.model.Proxy
import com.kinandcarta.create.proxytoggle.core.model.ProxyMapper
import com.kinandcarta.create.proxytoggle.core.settings.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceSettingsManagerImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val proxyMapper: ProxyMapper,
    private val proxyUpdateNotifier: ProxyUpdateNotifier,
    private val appSettings: AppSettings
) : DeviceSettingsManager {

    private val contentResolver by lazy { context.contentResolver }

    private val _proxySetting = MutableStateFlow(Proxy.Disabled)
    override val proxySetting = _proxySetting.asStateFlow()

    init {
        updateProxyData()
    }

    override fun enableProxy(proxy: Proxy) {
        Settings.Global.putString(
            contentResolver,
            Settings.Global.HTTP_PROXY,
            proxy.toString()
        )
        appSettings.lastUsedProxy = proxy
        updateProxyData()
    }

    override fun disableProxy() {
        Settings.Global.putString(
            contentResolver,
            Settings.Global.HTTP_PROXY,
            Proxy.Disabled.toString()
        )
        updateProxyData()
    }

    private fun updateProxyData() {
        val proxySetting = Settings.Global.getString(contentResolver, Settings.Global.HTTP_PROXY)
        _proxySetting.value = proxyMapper.from(proxySetting)
        proxyUpdateNotifier.notifyProxyChanged()
    }
}
