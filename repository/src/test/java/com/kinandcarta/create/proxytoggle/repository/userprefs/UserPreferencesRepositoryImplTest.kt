package com.kinandcarta.create.proxytoggle.repository.userprefs

import android.app.Application
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.datastore.UserPreferences
import com.kinandcarta.create.proxytoggle.repository.datastore.UserPreferencesSerializer
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class UserPreferencesRepositoryImplTest {

    private lateinit var testDataStore: DataStore<UserPreferences>

    private lateinit var subject: UserPreferencesRepositoryImpl

    private val context = ApplicationProvider.getApplicationContext<Application>()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this)

        testDataStore = DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { context.dataStoreFile("fake_user_preferences.pb") }
        )

        subject = UserPreferencesRepositoryImpl(testDataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isNightMode - when dataStore LIGHT then false`() = runTest {
        subject.isNightMode.test {
            testDataStore.updateData { userPrefsWithTheme(UserPreferences.ThemeMode.LIGHT) }
            assertThat(expectMostRecentItem()).isFalse()
        }
    }

    @Test
    fun `isNightMode - when dataStore DARK then true`() = runTest {
        subject.isNightMode.test {
            testDataStore.updateData { userPrefsWithTheme(UserPreferences.ThemeMode.DARK) }
            assertThat(expectMostRecentItem()).isTrue()
        }
    }

    @Test
    fun `isNightMode - when dataStore UNSPECIFIED then false`() = runTest {
        subject.isNightMode.test {
            testDataStore.updateData { userPrefsWithTheme(UserPreferences.ThemeMode.UNSPECIFIED) }
            assertThat(expectMostRecentItem()).isFalse()
        }
    }

    @Test
    fun `GIVEN dataStore LIGHT, WHEN toggleTheme(), THEN isNightMode becomes true`() = runTest {
        subject.isNightMode.test {
            testDataStore.updateData { userPrefsWithTheme(UserPreferences.ThemeMode.LIGHT) }
            subject.toggleTheme()
            assertThat(expectMostRecentItem()).isTrue()
        }
    }

    @Test
    fun `GIVEN dataStore DARK, WHEN toggleTheme(), THEN isNightMode becomes false`() = runTest {
        subject.isNightMode.test {
            testDataStore.updateData { userPrefsWithTheme(UserPreferences.ThemeMode.DARK) }
            subject.toggleTheme()
            assertThat(expectMostRecentItem()).isFalse()
        }
    }

    @Test
    fun `GIVEN dataStore UNSPECIFIED, WHEN toggleTheme(), THEN isNightMode becomes true`() {
        runTest {
            subject.isNightMode.test {
                testDataStore.updateData { userPrefsWithTheme(UserPreferences.ThemeMode.UNSPECIFIED) }
                subject.toggleTheme()
                assertThat(expectMostRecentItem()).isTrue()
            }
        }
    }

    private fun userPrefsWithTheme(themeMode: UserPreferences.ThemeMode): UserPreferences {
        return UserPreferences.newBuilder()
            .setThemeMode(themeMode)
            .build()
    }
}
