package com.kinandcarta.create.proxytoggle.widget.broadcast

import com.kinandcarta.create.proxytoggle.core.broadcast.ProxyUpdateListener
import com.kinandcarta.create.proxytoggle.core.broadcast.ProxyUpdateListenerProvider
import javax.inject.Inject

class ProxyUpdateListenerProviderImpl @Inject constructor(
    private val widgetProxyUpdateListener: WidgetProxyUpdateListener
) : ProxyUpdateListenerProvider {

    override val listeners: List<ProxyUpdateListener>
        get() = listOf(widgetProxyUpdateListener)
}
