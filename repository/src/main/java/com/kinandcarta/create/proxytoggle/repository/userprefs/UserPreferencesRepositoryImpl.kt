package com.kinandcarta.create.proxytoggle.repository.userprefs

import androidx.datastore.core.DataStore
import com.kinandcarta.create.proxytoggle.datastore.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<UserPreferences>
) : UserPreferencesRepository {

    override val isNightMode: Flow<Boolean> by lazy {
        userPreferences.map {
            it.themeMode == UserPreferences.ThemeMode.DARK
        }.distinctUntilChanged()
    }

    override suspend fun toggleTheme() {
        dataStore.updateData { userPreferences ->
            val newThemeMode = if (userPreferences.themeMode == UserPreferences.ThemeMode.DARK) {
                UserPreferences.ThemeMode.LIGHT
            } else {
                UserPreferences.ThemeMode.DARK
            }
            userPreferences.toBuilder().setThemeMode(newThemeMode).build()
        }
    }

    private val userPreferences: Flow<UserPreferences> by lazy {
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }
    }
}
