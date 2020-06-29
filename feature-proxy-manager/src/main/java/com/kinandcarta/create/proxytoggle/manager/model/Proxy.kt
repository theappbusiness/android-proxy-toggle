package com.kinandcarta.create.proxytoggle.manager.model

data class Proxy(val address: String, val port: Int) {

    companion object {
        private const val DISABLED_ADDRESS = ""
        private const val DISABLED_PORT = 0
        private const val DELIMITER = ":"

        val Disabled = Proxy(DISABLED_ADDRESS, DISABLED_PORT)

        fun from(proxy: String?): Proxy {
            return proxy?.let {
                val (address, port) = proxy.split(DELIMITER)
                Proxy(address, port.toInt())
            } ?: Disabled
        }
    }

    val isEnabled: Boolean
        get() = this != Disabled

    override fun toString(): String {
        return "$address:$port"
    }

    private fun List<String>.second() = this[1]
}
