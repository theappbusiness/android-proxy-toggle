package com.kinandcarta.create.proxytoggle.feature.manager.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.awaitValue
import com.kinandcarta.create.proxytoggle.feature.manager.view.ProxyManagerEvent
import com.kinandcarta.create.proxytoggle.feature.manager.view.ProxyState
import com.kinandcarta.create.proxytoggle.model.Proxy
import com.kinandcarta.create.proxytoggle.stubs.Stubs.PROXY_ADDRESS
import com.kinandcarta.create.proxytoggle.stubs.Stubs.PROXY_PORT
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.excludeRecords
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ProxyManagerViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var mockDeviceSettingsManager: DeviceSettingsManager

    @MockK
    private lateinit var mockProxyValidator: com.kinandcarta.create.proxytoggle.android.ProxyValidator

    private val fakeLiveData = MutableLiveData(Proxy.Disabled)

    private lateinit var subject: ProxyManagerViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { mockDeviceSettingsManager.proxySetting } returns fakeLiveData
        excludeRecords { mockDeviceSettingsManager.proxySetting }

        // Mock LiveData emitter
        val proxy = slot<Proxy>()
        every { mockDeviceSettingsManager.enableProxy(capture(proxy)) } answers {
            fakeLiveData.value = proxy.captured
        }
        every { mockDeviceSettingsManager.disableProxy() } answers {
            fakeLiveData.value = Proxy.Disabled
        }

        every { mockProxyValidator.isValidIP(any()) } returns true
        every { mockProxyValidator.isValidPort(any()) } returns true

        subject = ProxyManagerViewModel(mockDeviceSettingsManager, mockProxyValidator)
    }

    @After
    fun tearDown() {
        confirmVerified(mockDeviceSettingsManager)
    }

    @Test
    fun `enableProxy() - calls the DeviceSettingsManager and proxyState is updated`() {
        subject.enableProxy(PROXY_ADDRESS, PROXY_PORT)

        verify { mockDeviceSettingsManager.enableProxy(Proxy(PROXY_ADDRESS, PROXY_PORT)) }
        assertThat(subject.proxyState.awaitValue()).isEqualTo(
            ProxyState.Enabled(
                PROXY_ADDRESS,
                PROXY_PORT
            )
        )
    }

    @Test
    fun `enableProxy() - invalid address triggers event and proxyState is Disconnected`() {
        every { mockProxyValidator.isValidIP(PROXY_ADDRESS) } returns false

        subject.enableProxy(PROXY_ADDRESS, PROXY_PORT)

        verify { mockDeviceSettingsManager wasNot Called }
        assertThat(subject.proxyEvent.awaitValue()).isEqualTo(ProxyManagerEvent.InvalidAddress)
        assertThat(subject.proxyState.awaitValue()).isEqualTo(ProxyState.Disabled())
    }

    @Test
    fun `enableProxy() - invalid port triggers event and proxyState is Disconnected`() {
        every { mockProxyValidator.isValidPort(PROXY_PORT) } returns false

        subject.enableProxy(PROXY_ADDRESS, PROXY_PORT)

        verify { mockDeviceSettingsManager wasNot Called }
        assertThat(subject.proxyEvent.awaitValue()).isEqualTo(ProxyManagerEvent.InvalidPort)
        assertThat(subject.proxyState.awaitValue()).isEqualTo(ProxyState.Disabled())
    }

    @Test
    fun `disableProxy() - calls the DeviceSettingsManager and proxyState is updated`() {
        subject.disableProxy()

        verify { mockDeviceSettingsManager.disableProxy() }
        assertThat(subject.proxyState.awaitValue()).isEqualTo(ProxyState.Disabled())
    }
}
