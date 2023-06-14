package com.kinandcarta.create.proxytoggle.repository.devicesettings

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.core.common.proxy.Proxy
import com.kinandcarta.create.proxytoggle.core.common.proxyupdate.ProxyUpdateNotifier
import com.kinandcarta.create.proxytoggle.core.common.stub.Stubs.PROXY
import com.kinandcarta.create.proxytoggle.core.common.stub.Stubs.PROXY_DISABLED
import com.kinandcarta.create.proxytoggle.core.common.stub.Stubs.VALID_PROXY
import com.kinandcarta.create.proxytoggle.repository.appdata.AppDataRepository
import com.kinandcarta.create.proxytoggle.repository.proxymapper.ProxyMapper
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class DeviceSettingsManagerImplTest {

    @MockK
    private lateinit var mockProxyMapper: ProxyMapper

    @RelaxedMockK
    private lateinit var mockProxyUpdateNotifier: ProxyUpdateNotifier

    @RelaxedMockK
    private lateinit var mockAppDataRepository: AppDataRepository

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit var subject: DeviceSettingsManagerImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { mockProxyMapper.from(VALID_PROXY) } returns PROXY
        every { mockProxyMapper.from(PROXY_DISABLED) } returns Proxy.Disabled
        every { mockProxyMapper.from(null) } returns Proxy.Disabled

        subject = DeviceSettingsManagerImpl(
            context,
            mockProxyMapper,
            mockProxyUpdateNotifier,
            mockAppDataRepository
        )
    }

    @After
    fun tearDown() {
        confirmVerified(mockProxyUpdateNotifier, mockAppDataRepository)
    }

    @Test
    fun `initial state is disabled`() {
        assertThat(subject.proxySetting.value).isEqualTo(Proxy.Disabled)
        verify { mockProxyUpdateNotifier.notifyProxyChanged() }
    }

    @Test
    fun `enableProxy() - applies given proxy and proxySetting is updated and proxy is stored`() {
        runTest {
            subject.enableProxy(PROXY)

            assertThat(subject.proxySetting.value).isEqualTo(PROXY)
            coVerify {
                mockProxyUpdateNotifier.notifyProxyChanged()
                mockAppDataRepository.saveProxy(PROXY)
            }
        }
    }

    @Test
    fun `disableProxy() - final state is disabled`() {
        subject.disableProxy()

        assertThat(subject.proxySetting.value).isEqualTo(Proxy.Disabled)
        verify { mockProxyUpdateNotifier.notifyProxyChanged() }
    }
}
