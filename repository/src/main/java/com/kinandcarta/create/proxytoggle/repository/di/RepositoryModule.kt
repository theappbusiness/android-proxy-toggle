package com.kinandcarta.create.proxytoggle.repository.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.kinandcarta.create.proxytoggle.datastore.AppData
import com.kinandcarta.create.proxytoggle.datastore.UserPreferences
import com.kinandcarta.create.proxytoggle.repository.appdata.AppDataRepository
import com.kinandcarta.create.proxytoggle.repository.appdata.AppDataRepositoryImpl
import com.kinandcarta.create.proxytoggle.repository.datastore.AppDataSerializer
import com.kinandcarta.create.proxytoggle.repository.datastore.UserPreferencesSerializer
import com.kinandcarta.create.proxytoggle.repository.datastore.appDataMigration
import com.kinandcarta.create.proxytoggle.repository.datastore.userPreferencesMigration
import com.kinandcarta.create.proxytoggle.repository.proxymapper.ProxyMapper
import com.kinandcarta.create.proxytoggle.repository.userprefs.UserPreferencesRepository
import com.kinandcarta.create.proxytoggle.repository.userprefs.UserPreferencesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindAppDataRepository(
        appDataRepository: AppDataRepositoryImpl
    ): AppDataRepository

    @Binds
    fun bindUserPreferencesRepository(
        userPreferencesRepository: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    companion object {
        private const val APP_DATA_FILE_NAME = "app_data.pb"
        private const val USER_PREFERENCES_FILE_NAME = "user_preferences.pb"

        @Singleton
        @Provides
        fun provideAppDataDataStore(
            @ApplicationContext context: Context,
            proxyMapper: ProxyMapper
        ): DataStore<AppData> {
            return DataStoreFactory.create(
                serializer = AppDataSerializer,
                corruptionHandler = ReplaceFileCorruptionHandler(
                    produceNewData = { AppData.getDefaultInstance() }
                ),
                migrations = listOf(appDataMigration(context, proxyMapper)),
                produceFile = { context.dataStoreFile(APP_DATA_FILE_NAME) },
            )
        }

        @Singleton
        @Provides
        fun provideUserPreferencesDataStore(
            @ApplicationContext context: Context
        ): DataStore<UserPreferences> {
            return DataStoreFactory.create(
                serializer = UserPreferencesSerializer,
                corruptionHandler = ReplaceFileCorruptionHandler(
                    produceNewData = { UserPreferences.getDefaultInstance() }
                ),
                migrations = listOf(userPreferencesMigration(context)),
                produceFile = { context.dataStoreFile(USER_PREFERENCES_FILE_NAME) }
            )
        }
    }
}
