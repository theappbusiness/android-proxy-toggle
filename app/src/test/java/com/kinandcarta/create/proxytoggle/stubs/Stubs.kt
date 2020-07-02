package com.kinandcarta.create.proxytoggle.stubs

import com.kinandcarta.create.proxytoggle.model.Proxy

object Stubs {

    const val PROXY_ADDRESS = "1.2.3.4"
    const val PROXY_PORT = "515"

    const val VALID_PROXY = "$PROXY_ADDRESS:$PROXY_PORT"
    const val INVALID_PROXY = "$PROXY_ADDRESS:"
    const val PROXY_DISABLED = ":0"

    val PROXY = Proxy(PROXY_ADDRESS, PROXY_PORT)
}
