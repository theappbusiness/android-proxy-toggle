package com.kinandcarta.create.proxytoggle.manager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kinandcarta.create.proxytoggle.core.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.core.android.ProxyValidator
import com.kinandcarta.create.proxytoggle.core.android.ThemeSwitcher
import com.kinandcarta.create.proxytoggle.core.extension.SingleLiveEvent
import com.kinandcarta.create.proxytoggle.core.model.Proxy
import com.kinandcarta.create.proxytoggle.core.settings.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProxyManagerViewModel @Inject constructor(
    private val deviceSettingsManager: DeviceSettingsManager,
    private val proxyValidator: ProxyValidator,
    private val appSettings: AppSettings,
    private val themeSwitcher: ThemeSwitcher
) : ViewModel() {

    val proxyEvent = SingleLiveEvent<ProxyManagerEvent>()

    val proxyState: LiveData<ProxyState> = Transformations.map(deviceSettingsManager.proxySetting) {
        if (it.isEnabled) {
            ProxyState.Enabled(it.address, it.port)
        } else ProxyState.Disabled()
    }

    private val _isNightMode = MutableStateFlow(themeSwitcher.isNightMode())
    val isNightMode = _isNightMode.asStateFlow()

    val lastUsedProxy: Proxy
        get() = appSettings.lastUsedProxy

    fun enableProxy(address: String, port: String) {
        proxyEvent.value = ProxyManagerEvent.NoError
        when {
            !proxyValidator.isValidIP(address) -> {
                proxyEvent.value = ProxyManagerEvent.InvalidAddress
            }
            !proxyValidator.isValidPort(port) -> {
                proxyEvent.value = ProxyManagerEvent.InvalidPort
            }
            else -> {
                deviceSettingsManager.enableProxy(Proxy(address, port))
            }
        }
    }

    fun disableProxy() {
        deviceSettingsManager.disableProxy()
    }

    fun toggleTheme() {
        themeSwitcher.toggleTheme()
        _isNightMode.value = themeSwitcher.isNightMode()
    }
}
