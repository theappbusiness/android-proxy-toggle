package com.kinandcarta.create.proxytoggle.core.common.proxy

data class Proxy(val address: String, val port: String) {

    companion object {
        private const val DISABLED_ADDRESS = ""
        private const val DISABLED_PORT = "0"

        val Disabled = Proxy(DISABLED_ADDRESS, DISABLED_PORT)
    }

    val isEnabled: Boolean
        get() = this != Disabled

    override fun toString(): String {
        return "$address:$port"
    }
}
