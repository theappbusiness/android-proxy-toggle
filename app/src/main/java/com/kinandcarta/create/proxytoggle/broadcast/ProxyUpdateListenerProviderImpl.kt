package com.kinandcarta.create.proxytoggle.broadcast

import com.kinandcarta.create.proxytoggle.lib.core.broadcast.ProxyUpdateListener
import com.kinandcarta.create.proxytoggle.lib.core.broadcast.ProxyUpdateListenerProvider
import com.kinandcarta.create.proxytoggle.widget.broadcast.WidgetProxyUpdateListener
import javax.inject.Inject

class ProxyUpdateListenerProviderImpl @Inject constructor(
    private val widgetProxyUpdateListener: WidgetProxyUpdateListener
) : ProxyUpdateListenerProvider {

    override val listeners: List<ProxyUpdateListener>
        get() = listOf(widgetProxyUpdateListener)
}
