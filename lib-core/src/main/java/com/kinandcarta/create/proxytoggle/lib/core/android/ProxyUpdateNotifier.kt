package com.kinandcarta.create.proxytoggle.lib.core.android

import com.kinandcarta.create.proxytoggle.lib.core.broadcast.ProxyUpdateListenerProvider
import javax.inject.Inject

class ProxyUpdateNotifier @Inject constructor(
    private val listenerProvider: ProxyUpdateListenerProvider
) {

    fun notifyProxyChanged() {
        listenerProvider.listeners.forEach { it.onProxyUpdate() }
    }
}
