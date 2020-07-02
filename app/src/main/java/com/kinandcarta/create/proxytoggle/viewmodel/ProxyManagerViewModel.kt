package com.kinandcarta.create.proxytoggle.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kinandcarta.create.proxytoggle.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.android.ProxyValidator
import com.kinandcarta.create.proxytoggle.extensions.SingleLiveEvent
import com.kinandcarta.create.proxytoggle.model.Proxy
import com.kinandcarta.create.proxytoggle.view.manager.ProxyManagerEvent
import com.kinandcarta.create.proxytoggle.view.manager.ProxyState

class ProxyManagerViewModel @ViewModelInject constructor(
    private val deviceSettingsManager: DeviceSettingsManager,
    private val proxyValidator: ProxyValidator
) : ViewModel() {

    val proxyEvent = SingleLiveEvent<ProxyManagerEvent>()

    val proxyState = Transformations.map(deviceSettingsManager.proxySetting) { proxy ->
        if (proxy.isEnabled) {
            ProxyState.Enabled(proxy.address, proxy.port)
        } else ProxyState.Disabled()
    }

    fun enableProxy(address: String, port: String) {
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
}
