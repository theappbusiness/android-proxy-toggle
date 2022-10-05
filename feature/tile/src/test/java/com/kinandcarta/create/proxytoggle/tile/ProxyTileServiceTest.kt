package com.kinandcarta.create.proxytoggle.tile

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.service.quicksettings.Tile
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.core.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.core.model.Proxy
import com.kinandcarta.create.proxytoggle.core.settings.AppSettings
import com.kinandcarta.create.proxytoggle.core.stub.Stubs.PROXY
import com.kinandcarta.create.proxytoggle.testutils.expectedLaunchIntent
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ProxyTileServiceTest {

    @RelaxedMockK
    lateinit var mockDeviceSettingsManager: DeviceSettingsManager

    @RelaxedMockK
    lateinit var mockAppSettings: AppSettings

    @RelaxedMockK
    lateinit var mockTile: Tile

    @RelaxedMockK
    lateinit var mockContext: Context

    @MockK
    lateinit var mockPackageManager: PackageManager

    private lateinit var subject: ProxyTileService

    private val intent = slot<Intent>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { mockAppSettings.lastUsedProxy } returns PROXY
        every { mockContext.packageManager } returns mockPackageManager

        subject = spyk(ProxyTileService()) {
            deviceSettingsManager = mockDeviceSettingsManager
            appSettings = mockAppSettings
            every { baseContext } returns mockContext
            every { qsTile } returns mockTile
            every { startActivityAndCollapse(capture(intent)) } returns Unit
            every { getString(R.string.no_proxy_tile) } returns "No proxy"
        }
    }

    @Test
    fun `onClick() - GIVEN I have no proxy set up WHEN I click THEN proxy is enabled AND tile is updated`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns Proxy.Disabled andThen PROXY
        }

        subject.onClick()

        verify { mockDeviceSettingsManager.enableProxy(PROXY) }
        verifyTileIsEnabled(PROXY.toString())
    }

    @Test
    fun `onClick() - GIVEN proxy is disabled WHEN I click THEN proxy is enabled AND tile is updated`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns Proxy.Disabled andThen PROXY
        }

        subject.onClick()

        verify { mockDeviceSettingsManager.enableProxy(PROXY) }
        verifyTileIsEnabled(PROXY.toString())
    }

    @Test
    fun `onClick() - GIVEN proxy is disabled AND no last used proxy WHEN I click THEN the MainActivity is launched`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns Proxy.Disabled
        }

        every { mockAppSettings.lastUsedProxy } returns Proxy.Disabled

        val expectedIntent = expectedLaunchIntent(mockContext)
        every { mockPackageManager.getLaunchIntentForPackage(any()) } returns expectedIntent

        subject.onClick()

        verify(exactly = 0) { mockDeviceSettingsManager.enableProxy(any()) }
        verifyTileIsDisabled()
        assertThat(intent.captured.toUri(0)).isEqualTo(expectedIntent.toUri(0))
    }

    @Test
    fun `onClick() - GIVEN proxy is enabled WHEN I click THEN proxy is disabled AND tile is updated`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns PROXY andThen Proxy.Disabled
        }

        subject.onClick()

        verify { mockDeviceSettingsManager.disableProxy() }
        verifyTileIsDisabled()
    }

    @Test
    fun `onStartListening() - GIVEN I have no proxy set up THEN tile updates to disabled state`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns Proxy.Disabled
        }

        subject.onStartListening()

        verifyTileIsDisabled()
    }

    @Test
    fun `onStartListening() - GIVEN proxy is disabled THEN tile updates to disabled state`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns Proxy.Disabled
        }

        subject.onStartListening()

        verifyTileIsDisabled()
    }

    @Test
    fun `onStartListening() - GIVEN proxy is enabled THEN tile updates to enabled state`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns PROXY
        }

        subject.onStartListening()

        verifyTileIsEnabled(PROXY.toString())
    }

    private fun verifyTileIsEnabled(label: String) {
        verify {
            mockTile.label = label
            mockTile.state = Tile.STATE_ACTIVE
            mockTile.updateTile()
        }
        confirmVerified(mockTile)
    }

    private fun verifyTileIsDisabled() {
        verify {
            mockTile.label = "No proxy"
            mockTile.state = Tile.STATE_INACTIVE
            mockTile.updateTile()
        }
        confirmVerified(mockTile)
    }
}
