package com.kinandcarta.create.proxytoggle.manager.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kinandcarta.create.proxytoggle.manager.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.manager.android.ProxyValidator
import com.kinandcarta.create.proxytoggle.manager.extensions.asLiveData
import com.kinandcarta.create.proxytoggle.manager.model.Proxy
import com.kinandcarta.create.proxytoggle.manager.view.ProxyManagerEvent
import com.kinandcarta.create.proxytoggle.manager.view.ProxyState

class ProxyManagerViewModel @ViewModelInject constructor(
    private val deviceSettingsManager: DeviceSettingsManager,
    private val proxyValidator: ProxyValidator
) : ViewModel() {

    private val _proxyEvent = MutableLiveData<ProxyManagerEvent>()
    val proxyEvent = _proxyEvent.asLiveData()

    val proxyState = Transformations.map(deviceSettingsManager.proxySetting) { proxy ->
        if (proxy.isEnabled) {
            ProxyState.Enabled(proxy.address, proxy.port)
        } else ProxyState.Disabled()
    }

    fun enableProxy(address: String, port: String) {
        if (!proxyValidator.isValidIP(address)) {
            _proxyEvent.value = ProxyManagerEvent.InvalidAddress
        } else if (!proxyValidator.isValidPort(port)) {
            _proxyEvent.value = ProxyManagerEvent.InvalidPort
        } else {
            deviceSettingsManager.enableProxy(Proxy(address, port))
        }
    }

    fun disableProxy() {
        deviceSettingsManager.disableProxy()
    }
}
