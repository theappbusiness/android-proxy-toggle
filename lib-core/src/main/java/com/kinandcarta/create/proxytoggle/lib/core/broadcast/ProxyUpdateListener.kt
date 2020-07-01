package com.kinandcarta.create.proxytoggle.lib.core.broadcast

interface ProxyUpdateListenerProvider {
    val listeners: List<ProxyUpdateListener>
}

interface ProxyUpdateListener {
    fun onProxyUpdate()
}
