package com.kinandcarta.create.proxytoggle.repository.di

import com.kinandcarta.create.proxytoggle.repository.devicesettings.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.repository.devicesettings.DeviceSettingsManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DeviceSettingsModule {

    @Binds
    fun bindDeviceSettingsManager(
        deviceSettingsManager: DeviceSettingsManagerImpl
    ): DeviceSettingsManager
}
