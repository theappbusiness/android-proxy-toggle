package com.kinandcarta.create.proxytoggle.manager.model

import javax.inject.Inject

class ProxyMapper @Inject constructor() {

    companion object {
        private const val DELIMITER = ":"
    }

    fun from(proxy: String?): Proxy {
        return proxy?.let {
            try {
                val (address, port) = proxy.split(DELIMITER)
                Proxy(address, port.toInt())
            } catch (ignored: Exception) {
                Proxy.Disabled
            }
        } ?: Proxy.Disabled
    }
}
