package com.kinandcarta.create.proxytoggle.manager.mapper

import android.content.Context
import com.kinandcarta.create.proxytoggle.core.model.Proxy
import com.kinandcarta.create.proxytoggle.manager.R
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerEvent
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProxyUiDataMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getUserInputErrors(proxyEvent: ProxyManagerEvent?): InputErrors {
        return when (proxyEvent) {
            ProxyManagerEvent.InvalidAddress -> {
                InputErrors(context.getString(R.string.error_invalid_address), null)
            }
            ProxyManagerEvent.InvalidPort -> {
                InputErrors(null, context.getString(R.string.error_invalid_port))
            }
            ProxyManagerEvent.NoError -> InputErrors(null, null)
            null -> InputErrors(null, null)
        }
    }

    fun getActiveProxyData(proxyState: ProxyState?): AddressAndPort? {
        return when (proxyState) {
            is ProxyState.Enabled -> AddressAndPort(proxyState.address, proxyState.port)
            else -> null
        }
    }

    fun getLastProxyUsedData(lastProxyUsed: Proxy): AddressAndPort? {
        return when {
            lastProxyUsed.isEnabled -> {
                AddressAndPort(lastProxyUsed.address, lastProxyUsed.port)
            }
            else -> null
        }
    }
}

data class InputErrors(val addressError: String?, val portError: String?)

data class AddressAndPort(val address: String, val port: String)
