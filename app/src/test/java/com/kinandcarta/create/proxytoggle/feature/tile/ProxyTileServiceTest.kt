package com.kinandcarta.create.proxytoggle.feature.tile

import android.service.quicksettings.Tile
import com.kinandcarta.create.proxytoggle.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.model.Proxy
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ProxyTileServiceTest {

    companion object {
        // TODO Change this when we have valid input data from user
        private val PROXY = Proxy("192.168.1.215", "8888")
    }

    @RelaxedMockK
    lateinit var mockDeviceSettingsManager: DeviceSettingsManager

    @RelaxedMockK
    lateinit var mockTile: Tile

    private lateinit var subject: ProxyTileService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = spyk(ProxyTileService()) {
            deviceSettingsManager = mockDeviceSettingsManager
            every { qsTile } returns mockTile
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
