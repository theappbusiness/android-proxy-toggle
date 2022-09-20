package com.kinandcarta.create.proxytoggle.injection

import com.kinandcarta.create.proxytoggle.android.DeviceSettingsManagerImpl
import com.kinandcarta.create.proxytoggle.android.SharedPrefsAppSettings
import com.kinandcarta.create.proxytoggle.core.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.core.settings.AppSettings
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun bindAppSettings(
        sharedPrefsAppSettings: SharedPrefsAppSettings
    ): AppSettings

    @Binds
    fun bindDeviceSettingsManager(
        deviceSettingsManager: DeviceSettingsManagerImpl
    ): DeviceSettingsManager
}
