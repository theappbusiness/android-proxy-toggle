package com.kinandcarta.create.proxytoggle.broadcast

import javax.inject.Inject

class ProxyUpdateListenerProviderImpl @Inject constructor(
    private val widgetProxyUpdateListener: WidgetProxyUpdateListener
) : ProxyUpdateListenerProvider {

    override val listeners: List<ProxyUpdateListener>
        get() = listOf(widgetProxyUpdateListener)
}
