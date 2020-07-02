package com.kinandcarta.create.proxytoggle.broadcast

interface ProxyUpdateListenerProvider {
    val listeners: List<ProxyUpdateListener>
}

interface ProxyUpdateListener {
    fun onProxyUpdate()
}
