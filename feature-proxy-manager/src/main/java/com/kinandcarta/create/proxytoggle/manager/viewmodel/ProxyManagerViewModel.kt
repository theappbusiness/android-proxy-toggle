package com.kinandcarta.create.proxytoggle.manager.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kinandcarta.create.proxytoggle.manager.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.manager.android.ProxyValidator
import com.kinandcarta.create.proxytoggle.manager.extensions.SingleLiveEvent
import com.kinandcarta.create.proxytoggle.manager.model.Proxy
import com.kinandcarta.create.proxytoggle.manager.view.ProxyManagerEvent
import com.kinandcarta.create.proxytoggle.manager.view.ProxyState

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
        if (!proxyValidator.isValidIP(address)) {
            proxyEvent.value = ProxyManagerEvent.InvalidAddress
        } else if (!proxyValidator.isValidPort(port)) {
            proxyEvent.value = ProxyManagerEvent.InvalidPort
        } else {
            deviceSettingsManager.enableProxy(Proxy(address, port))
        }
    }

    fun disableProxy() {
        deviceSettingsManager.disableProxy()
    }
}
