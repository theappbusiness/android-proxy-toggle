package com.kinandcarta.create.proxytoggle.core.common.proxy

import androidx.core.text.isDigitsOnly
import androidx.core.util.PatternsCompat
import javax.inject.Inject

class ProxyValidator @Inject constructor() {

    companion object {
        const val MAX_PORT = 65535
    }

    fun isValidIP(input: String): Boolean {
        return PatternsCompat.IP_ADDRESS.matcher(input).matches()
    }

    fun isValidPort(input: String): Boolean {
        return input.isNotBlank() &&
            input.isDigitsOnly() &&
            input.toInt() in 1..MAX_PORT
    }
}
