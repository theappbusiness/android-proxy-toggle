package com.kinandcarta.create.proxytoggle.manager.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.manager.awaitValue
import com.kinandcarta.create.proxytoggle.manager.model.Proxy
import com.kinandcarta.create.proxytoggle.manager.view.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.manager.view.ProxyState
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ProxyManagerViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    companion object {
        private const val ADDRESS = "1.2.3.4"
        private const val PORT = "515"
    }

    @RelaxedMockK
    private lateinit var mockDeviceSettingsManager: DeviceSettingsManager

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

        subject = ProxyManagerViewModel(mockDeviceSettingsManager)
    }

    @After
    fun tearDown() {
        confirmVerified(mockDeviceSettingsManager)
    }

    @Test
    fun `enableProxy() - calls the DeviceSettingsManager and proxyState is updated`() {
        subject.enableProxy(ADDRESS, PORT)

        verify { mockDeviceSettingsManager.enableProxy(Proxy(ADDRESS, PORT)) }
        assertThat(subject.proxyState.awaitValue()).isEqualTo(ProxyState.Enabled(ADDRESS, PORT))
    }

    @Test
    fun `disableProxy() - calls the DeviceSettingsManager and proxyState is updated`() {
        subject.disableProxy()

        verify { mockDeviceSettingsManager.disableProxy() }
        assertThat(subject.proxyState.awaitValue()).isEqualTo(ProxyState.Disabled)
    }
}