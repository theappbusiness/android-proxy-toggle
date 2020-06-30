package com.kinandcarta.create.proxytoggle.manager.model

import javax.inject.Inject

class ProxyMapper @Inject constructor() {

    companion object {
        private const val DELIMITER = ":"
    }

    fun from(proxy: String?) = proxy?.let {
        val (address, port) = proxy.split(DELIMITER)
        if (address.isBlank() || port.isBlank()) {
            Proxy.Disabled
        } else {
            Proxy(address, port)
        }
    } ?: Proxy.Disabled
}
