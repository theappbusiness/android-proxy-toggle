package com.kinandcarta.create.proxytoggle.injection

import com.kinandcarta.create.proxytoggle.android.SharedPrefsAppSettings
import com.kinandcarta.create.proxytoggle.broadcast.ProxyUpdateListenerProvider
import com.kinandcarta.create.proxytoggle.broadcast.ProxyUpdateListenerProviderImpl
import com.kinandcarta.create.proxytoggle.settings.AppSettings
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

    @Binds
    abstract fun bindAppSettings(
        sharedPrefsAppSettings: SharedPrefsAppSettings
    ): AppSettings
}
