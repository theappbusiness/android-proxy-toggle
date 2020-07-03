package com.kinandcarta.create.proxytoggle.android

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.model.Proxy
import com.kinandcarta.create.proxytoggle.model.ProxyMapper
import com.kinandcarta.create.proxytoggle.settings.AppSettings
import com.kinandcarta.create.proxytoggle.stubs.Stubs.PROXY
import com.kinandcarta.create.proxytoggle.stubs.Stubs.PROXY_DISABLED
import com.kinandcarta.create.proxytoggle.stubs.Stubs.VALID_PROXY
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class DeviceSettingsManagerTest {

    @MockK
    private lateinit var mockProxyMapper: ProxyMapper

    @RelaxedMockK
    private lateinit var mockProxyUpdateNotifier: ProxyUpdateNotifier

    @RelaxedMockK
    private lateinit var mockAppSettings: AppSettings

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit var subject: DeviceSettingsManager

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { mockProxyMapper.from(VALID_PROXY) } returns PROXY
        every { mockProxyMapper.from(PROXY_DISABLED) } returns Proxy.Disabled
        every { mockProxyMapper.from(null) } returns Proxy.Disabled

        subject = DeviceSettingsManager(context, mockProxyMapper, mockProxyUpdateNotifier, mockAppSettings)
    }

    @After
    fun tearDown() {
        confirmVerified(mockProxyUpdateNotifier, mockAppSettings)
    }

    @Test
    fun `initial state is disabled`() {
        assertThat(subject.proxySetting.value).isEqualTo(Proxy.Disabled)
        verify { mockProxyUpdateNotifier.notifyProxyChanged() }
    }

    @Test
    fun `enableProxy() - applies given proxy and proxySetting is updated and proxy is stored`() {
        subject.enableProxy(PROXY)

        assertThat(subject.proxySetting.value).isEqualTo(PROXY)
        verify {
            mockProxyUpdateNotifier.notifyProxyChanged()
            mockAppSettings.lastUsedProxy = PROXY
        }
    }

    @Test
    fun `disableProxy() - final state is disabled`() {
        subject.disableProxy()

        assertThat(subject.proxySetting.value).isEqualTo(Proxy.Disabled)
        verify { mockProxyUpdateNotifier.notifyProxyChanged() }
    }
}
