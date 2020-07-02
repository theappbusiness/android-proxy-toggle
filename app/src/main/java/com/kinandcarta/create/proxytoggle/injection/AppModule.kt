package com.kinandcarta.create.proxytoggle.injection

import com.kinandcarta.create.proxytoggle.broadcast.ProxyUpdateListenerProvider
import com.kinandcarta.create.proxytoggle.broadcast.ProxyUpdateListenerProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindProxyUpdateListenerProvider(
        providerImpl: ProxyUpdateListenerProviderImpl
    ): ProxyUpdateListenerProvider
}
