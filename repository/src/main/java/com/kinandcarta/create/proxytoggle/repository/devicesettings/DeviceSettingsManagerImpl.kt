package com.kinandcarta.create.proxytoggle.repository.devicesettings

import android.content.Context
import android.provider.Settings
import com.kinandcarta.create.proxytoggle.core.common.proxy.Proxy
import com.kinandcarta.create.proxytoggle.core.common.proxyupdate.ProxyUpdateNotifier
import com.kinandcarta.create.proxytoggle.repository.appdata.AppDataRepository
import com.kinandcarta.create.proxytoggle.repository.proxymapper.ProxyMapper
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
    private val appDataRepository: AppDataRepository
) : DeviceSettingsManager {

    private val contentResolver by lazy { context.contentResolver }

    private val _proxySetting = MutableStateFlow(Proxy.Disabled)
    override val proxySetting = _proxySetting.asStateFlow()

    init {
        updateProxyData()
    }

    override suspend fun enableProxy(proxy: Proxy) {
        Settings.Global.putString(
            contentResolver,
            Settings.Global.HTTP_PROXY,
            proxy.toString()
        )
        appDataRepository.saveProxy(proxy)
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
