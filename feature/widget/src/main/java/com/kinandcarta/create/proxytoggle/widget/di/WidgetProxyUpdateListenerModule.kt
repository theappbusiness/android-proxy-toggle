package com.kinandcarta.create.proxytoggle.widget.di

import com.kinandcarta.create.proxytoggle.core.common.proxyupdate.ProxyUpdateListener
import com.kinandcarta.create.proxytoggle.widget.broadcast.WidgetProxyUpdateListener
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface WidgetProxyUpdateListenerModule {

    @Binds
    @IntoSet
    fun bindWidgetProxyUpdateListener(
        widgetListener: WidgetProxyUpdateListener
    ): ProxyUpdateListener
}
