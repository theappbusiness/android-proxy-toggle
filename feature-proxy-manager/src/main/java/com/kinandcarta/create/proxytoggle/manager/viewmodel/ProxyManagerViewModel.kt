package com.kinandcarta.create.proxytoggle.manager.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kinandcarta.create.proxytoggle.manager.model.Proxy
import com.kinandcarta.create.proxytoggle.manager.view.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.manager.view.ProxyState

class ProxyManagerViewModel @ViewModelInject constructor(
    private val deviceSettingsManager: DeviceSettingsManager
) : ViewModel() {

    val proxyState = Transformations.map(deviceSettingsManager.proxySetting) { proxy ->
        if (proxy.isEnabled) {
            ProxyState.Enabled(proxy.address, proxy.port)
        } else ProxyState.Disabled
    }

    fun enableProxy(address: String, port: String) {
        deviceSettingsManager.enableProxy(Proxy(address, port))
    }

    fun disableProxy() {
        deviceSettingsManager.disableProxy()
    }
}
