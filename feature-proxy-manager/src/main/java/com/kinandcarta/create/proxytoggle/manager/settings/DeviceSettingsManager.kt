package com.kinandcarta.create.proxytoggle.manager.settings

import android.content.Context
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kinandcarta.create.proxytoggle.manager.model.Proxy
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeviceSettingsManager @Inject constructor(@ApplicationContext context: Context) {

    private val contentResolver by lazy { context.contentResolver }

    private val _proxySetting = MutableLiveData<Proxy>()

    val proxySetting: LiveData<Proxy>
        get() = _proxySetting

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
        _proxySetting.value = Proxy.from(proxySetting)
    }
}
