package com.kinandcarta.create.proxytoggle.feature.tile

import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.model.Proxy
import com.kinandcarta.create.proxytoggle.settings.AppSettings
import com.kinandcarta.create.proxytoggle.stubs.Stubs.PROXY
import com.kinandcarta.create.proxytoggle.view.MainActivity
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkStatic
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

    private lateinit var subject: ProxyTileService

    private val intent = slot<Intent>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(MainActivity::class)

        every { mockAppSettings.lastUsedProxy } returns PROXY

        subject = spyk(ProxyTileService()) {
            deviceSettingsManager = mockDeviceSettingsManager
            appSettings = mockAppSettings
            every { baseContext } returns mockContext
            every { qsTile } returns mockTile
            every { startActivityAndCollapse(capture(intent)) } returns Unit
        }
    }

    @Test
    fun `onClick() - GIVEN I have no proxy set up WHEN I click the tile THEN the proxy enables AND the tile is updated`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns null andThen PROXY
        }

        subject.onClick()

        verify { mockDeviceSettingsManager.enableProxy(PROXY) }
        verifyTileIsEnabled(PROXY.toString())
    }

    @Test
    fun `onClick() - GIVEN the proxy is disabled WHEN I click the tile THEN the proxy enables AND the tile is updated`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns Proxy.Disabled andThen PROXY
        }

        subject.onClick()

        verify { mockDeviceSettingsManager.enableProxy(PROXY) }
        verifyTileIsEnabled(PROXY.toString())
    }

    @Test
    fun `onClick() - GIVEN the proxy is disabled AND I don't have a last used proxy WHEN I click the tile THEN the MainActivity is launched`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns Proxy.Disabled
        }

        every { mockAppSettings.lastUsedProxy } returns Proxy.Disabled

        subject.onClick()

        verify(exactly = 0) { mockDeviceSettingsManager.enableProxy(any()) }
        verifyTileIsDisabled()
        val expectedIntent = MainActivity.getIntent(mockContext)
        assertThat(intent.captured.toUri(0)).isEqualTo(expectedIntent.toUri(0))
    }

    @Test
    fun `onClick() - GIVEN the proxy is enabled WHEN I click the tile THEN the proxy disables AND the tile is updated`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns PROXY andThen Proxy.Disabled
        }

        subject.onClick()

        verify { mockDeviceSettingsManager.disableProxy() }
        verifyTileIsDisabled()
    }

    @Test
    fun `onStartListening() - GIVEN I have no proxy set up THEN the tile updates to disabled state`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns null
        }

        subject.onStartListening()

        verifyTileIsDisabled()
    }

    @Test
    fun `onStartListening() - GIVEN the proxy is disabled THEN the tile updates to disabled state`() {
        every { mockDeviceSettingsManager.proxySetting } returns mockk {
            every { value } returns Proxy.Disabled
        }

        subject.onStartListening()

        verifyTileIsDisabled()
    }

    @Test
    fun `onStartListening() - GIVEN the proxy is enabled THEN the tile updates to enabled state`() {
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
            mockTile.label = "Proxy Disabled"
            mockTile.state = Tile.STATE_INACTIVE
            mockTile.updateTile()
        }
        confirmVerified(mockTile)
    }
}
