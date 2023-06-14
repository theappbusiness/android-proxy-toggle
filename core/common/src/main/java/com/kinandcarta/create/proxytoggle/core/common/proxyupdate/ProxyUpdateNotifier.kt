package com.kinandcarta.create.proxytoggle.core.common.proxyupdate

import javax.inject.Inject

class ProxyUpdateNotifier @Inject constructor(
    private val listeners: @JvmSuppressWildcards Set<ProxyUpdateListener>
) {

    fun notifyProxyChanged() {
        listeners.forEach { it.onProxyUpdate() }
    }
}
