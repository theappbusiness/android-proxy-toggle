package com.kinandcarta.create.proxytoggle.core.common.stub

import com.kinandcarta.create.proxytoggle.core.common.proxy.Proxy

object Stubs {

    const val PROXY_ADDRESS = "1.2.3.4"
    const val PROXY_PORT = "515"

    const val VALID_PROXY = "$PROXY_ADDRESS:$PROXY_PORT"
    const val INVALID_PROXY_PORT = "$PROXY_ADDRESS:"
    const val INVALID_PROXY_ADDRESS = ":$PROXY_PORT"
    const val PROXY_DISABLED = ":0"

    val PROXY = Proxy(PROXY_ADDRESS, PROXY_PORT)
}
