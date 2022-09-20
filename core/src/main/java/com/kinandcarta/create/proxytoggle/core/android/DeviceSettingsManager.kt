package com.kinandcarta.create.proxytoggle.core.android

import com.kinandcarta.create.proxytoggle.core.model.Proxy
import kotlinx.coroutines.flow.StateFlow

interface DeviceSettingsManager {
    val proxySetting: StateFlow<Proxy>
    fun enableProxy(proxy: Proxy)
    fun disableProxy()
}
