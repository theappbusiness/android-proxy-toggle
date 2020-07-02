package com.kinandcarta.create.proxytoggle.feature.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kinandcarta.create.proxytoggle.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.model.Proxy
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class, sdk = [Build.VERSION_CODES.P])
class ToggleWidgetProviderTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @RelaxedMockK
    lateinit var mockDeviceSettingsManager: DeviceSettingsManager

    @MockK
    private lateinit var mockAppWidgetManager: AppWidgetManager

    private val context by lazy { ApplicationProvider.getApplicationContext<Context>() }

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
        val intent = Intent().apply { action = "Enable Proxy" }

        subject.onReceive(context, intent)

        verify {
            // TODO Take this from SharedPrefs once the user is able to input
            mockDeviceSettingsManager.enableProxy(Proxy("192.168.1.215", "8888"))
        }
        confirmVerified(mockDeviceSettingsManager)
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