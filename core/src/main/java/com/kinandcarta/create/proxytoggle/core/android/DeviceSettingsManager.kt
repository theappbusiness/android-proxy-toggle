package com.kinandcarta.create.proxytoggle.core.android

import androidx.lifecycle.LiveData
import com.kinandcarta.create.proxytoggle.core.model.Proxy

interface DeviceSettingsManager {
    val proxySetting: LiveData<Proxy>
    fun enableProxy(proxy: Proxy)
    fun disableProxy()
}
