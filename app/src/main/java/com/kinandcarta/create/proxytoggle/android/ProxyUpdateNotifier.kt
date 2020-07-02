package com.kinandcarta.create.proxytoggle.android

import com.kinandcarta.create.proxytoggle.broadcast.ProxyUpdateListenerProvider
import javax.inject.Inject

class ProxyUpdateNotifier @Inject constructor(
    private val listenerProvider: ProxyUpdateListenerProvider
) {

    fun notifyProxyChanged() {
        listenerProvider.listeners.forEach { it.onProxyUpdate() }
    }
}
