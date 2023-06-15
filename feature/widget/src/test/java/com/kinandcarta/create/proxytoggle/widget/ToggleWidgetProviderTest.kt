package com.kinandcarta.create.proxytoggle.widget

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.core.common.proxy.Proxy
import com.kinandcarta.create.proxytoggle.core.common.stub.Stubs
import com.kinandcarta.create.proxytoggle.repository.appdata.AppDataRepository
import com.kinandcarta.create.proxytoggle.repository.devicesettings.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.repository.di.DeviceSettingsModule
import com.kinandcarta.create.proxytoggle.repository.di.RepositoryModule
import com.kinandcarta.create.proxytoggle.testutils.addMainActivityToRobolectric
import com.kinandcarta.create.proxytoggle.testutils.expectedLaunchIntent
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@HiltAndroidTest
@UninstallModules(RepositoryModule::class, DeviceSettingsModule::class)
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
    lateinit var mockAppDataRepository: AppDataRepository

    @MockK
    private lateinit var mockAppWidgetManager: AppWidgetManager

    private val context = spyk(ApplicationProvider.getApplicationContext<Application>())

    private lateinit var subject: ToggleWidgetProvider

    private val ids = listOf(1, 2).toIntArray()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { mockAppWidgetManager.updateAppWidget(or(1, 2), any()) } returns Unit

        subject = ToggleWidgetProvider().apply {
            deviceSettingsManager = mockDeviceSettingsManager
            appDataRepository = mockAppDataRepository
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
    fun `onReceive() - WHEN I receive an enable action THEN deviceSettingsManager enables the proxy`() {
        every { mockAppDataRepository.pastProxies } returns flowOf(listOf(Stubs.PROXY))

        val intent = Intent().apply { action = "Enable Proxy" }

        subject.onReceive(context, intent)

        coVerify {
            mockDeviceSettingsManager.enableProxy(Stubs.PROXY)
        }
        confirmVerified(mockDeviceSettingsManager)
    }

    @Test
    fun `onReceive() - GIVEN no last used proxy WHEN I receive an enable action THEN MainActivity is launched`() {
        every { mockAppDataRepository.pastProxies } returns flowOf(emptyList())
        addMainActivityToRobolectric(context)

        subject.onReceive(context, Intent("Enable Proxy"))

        coVerify(exactly = 0) { mockDeviceSettingsManager.enableProxy(any()) }
        val nextIntent = shadowOf(context).peekNextStartedActivity()
        assertThat(nextIntent.toUri(0)).isEqualTo(expectedLaunchIntent(context).toUri(0))
    }

    @Test
    fun `onReceive() - WHEN I receive a disable action THEN deviceSettingsManager disables the proxy`() {
        val intent = Intent().apply { action = "Disable Proxy" }

        subject.onReceive(context, intent)

        verify {
            mockDeviceSettingsManager.disableProxy()
        }
        confirmVerified(mockDeviceSettingsManager)
    }
}
