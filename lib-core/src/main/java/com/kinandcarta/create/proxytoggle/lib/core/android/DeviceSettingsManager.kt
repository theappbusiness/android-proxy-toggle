package com.kinandcarta.create.proxytoggle.lib.core.android

import android.content.Context
import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import com.kinandcarta.create.proxytoggle.lib.core.extensions.asLiveData
import com.kinandcarta.create.proxytoggle.lib.core.model.Proxy
import com.kinandcarta.create.proxytoggle.lib.core.model.ProxyMapper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeviceSettingsManager @Inject constructor(
    @ApplicationContext context: Context,
    private val proxyMapper: ProxyMapper
) {

    private val contentResolver by lazy { context.contentResolver }

    private val _proxySetting = MutableLiveData<Proxy>()
    val proxySetting = _proxySetting.asLiveData()

    init {
        updateProxyData()
    }

    fun enableProxy(proxy: Proxy) {
        Settings.Global.putString(
            contentResolver,
            Settings.Global.HTTP_PROXY,
            proxy.toString()
        )
        updateProxyData()
    }

    fun disableProxy() {
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
    }
}
