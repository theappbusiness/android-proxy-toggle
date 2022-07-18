package com.kinandcarta.create.proxytoggle.widget.injection

import com.kinandcarta.create.proxytoggle.core.broadcast.ProxyUpdateListenerProvider
import com.kinandcarta.create.proxytoggle.widget.broadcast.ProxyUpdateListenerProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ProxyUpdateListenerProviderModule {

    @Suppress("unused")
    @Binds
    fun bindProxyUpdateListenerProvider(
        providerImpl: ProxyUpdateListenerProviderImpl
    ): ProxyUpdateListenerProvider
}
