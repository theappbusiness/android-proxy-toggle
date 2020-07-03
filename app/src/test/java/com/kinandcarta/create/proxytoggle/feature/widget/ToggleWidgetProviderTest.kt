package com.kinandcarta.create.proxytoggle.feature.widget

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.injection.AppModule
import com.kinandcarta.create.proxytoggle.model.Proxy
import com.kinandcarta.create.proxytoggle.settings.AppSettings
import com.kinandcarta.create.proxytoggle.stubs.Stubs
import com.kinandcarta.create.proxytoggle.view.MainActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@HiltAndroidTest
@UninstallModules(AppModule::class)
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class, sdk = [Build.VERSION_CODES.P])
class ToggleWidgetProviderTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @RelaxedMockK
    lateinit var mockDeviceSettingsManager: DeviceSettingsManager

    @BindValue
    @RelaxedMockK
    lateinit var mockAppSettings: AppSettings

    @MockK
    private lateinit var mockAppWidgetManager: AppWidgetManager

    private val context by lazy { ApplicationProvider.getApplicationContext<Application>() }

    private lateinit var subject: ToggleWidgetProvider

    private val ids = listOf(1, 2).toIntArray()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { mockAppWidgetManager.updateAppWidget(or(1, 2), any()) } returns Unit

        subject = ToggleWidgetProvider().apply {
            deviceSettingsManager = mockDeviceSettingsManager
        }
    }

    @Test
    fun `onUpdate() - we can't test a RemoteViews object but at least we make sure we update all widgets`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns Proxy.Disabled
        }

        subject.onUpdate(context, mockAppWidgetManager, ids)

        verify(exactly = ids.size) {
            mockAppWidgetManager.updateAppWidget(or(1, 2), any())
        }
        confirmVerified(mockAppWidgetManager)
    }

    @Test
    fun `onReceive() - GIVEN I receive an enable action THEN the deviceSettingsManager enables the proxy`() {
        every { mockAppSettings.lastUsedProxy } returns Stubs.PROXY

        val intent = Intent().apply { action = "Enable Proxy" }

        subject.onReceive(context, intent)

        verify {
            mockDeviceSettingsManager.enableProxy(Stubs.PROXY)
        }
        confirmVerified(mockDeviceSettingsManager)
    }

    @Test
    fun `onReceive() - GIVEN I receive an enable action AND I don't have a last used proxy THEN  the MainActivity is launched`() {
        every { mockAppSettings.lastUsedProxy } returns Proxy.Disabled

        val intent = Intent().apply { action = "Enable Proxy" }

        subject.onReceive(context, intent)

        verify(exactly = 0) { mockDeviceSettingsManager.enableProxy(any()) }
        val nextIntent = shadowOf(context).peekNextStartedActivity()
        val expectedIntent = MainActivity.getIntent(context).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        assertThat(nextIntent.toUri(0)).isEqualTo(expectedIntent.toUri(0))
    }

    @Test
    fun `onReceive() - GIVEN I receive a disable action THEN the deviceSettingsManager disables the proxy`() {
        val intent = Intent().apply { action = "Disable Proxy" }

        subject.onReceive(context, intent)

        verify {
            mockDeviceSettingsManager.disableProxy()
        }
        confirmVerified(mockDeviceSettingsManager)
    }
}