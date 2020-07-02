package com.kinandcarta.create.proxytoggle.broadcast

import com.kinandcarta.create.proxytoggle.feature.widget.broadcast.WidgetProxyUpdateListener
import javax.inject.Inject

class ProxyUpdateListenerProviderImpl @Inject constructor(
    private val widgetProxyUpdateListener: WidgetProxyUpdateListener
) : ProxyUpdateListenerProvider {

    override val listeners: List<ProxyUpdateListener>
        get() = listOf(widgetProxyUpdateListener)
}
