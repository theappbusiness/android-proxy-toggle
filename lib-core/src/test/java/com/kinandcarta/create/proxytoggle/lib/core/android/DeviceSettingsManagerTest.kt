package com.kinandcarta.create.proxytoggle.lib.core.android

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.lib.core.model.Proxy
import com.kinandcarta.create.proxytoggle.lib.core.model.ProxyMapper
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

    companion object {
        private const val PROXY_ENABLED = "1.2.3.4:515"
        private const val PROXY_DISABLED = ":0"
    }

    @MockK
    private lateinit var mockProxyMapper: ProxyMapper

    @RelaxedMockK
    private lateinit var mockProxyUpdateNotifier: ProxyUpdateNotifier

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit var subject: DeviceSettingsManager

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { mockProxyMapper.from(PROXY_ENABLED) } returns Proxy("1.2.3.4", "515")
        every { mockProxyMapper.from(PROXY_DISABLED) } returns Proxy.Disabled
        every { mockProxyMapper.from(null) } returns Proxy.Disabled

        subject = DeviceSettingsManager(context, mockProxyMapper, mockProxyUpdateNotifier)
    }

    @After
    fun tearDown(){
        confirmVerified(mockProxyUpdateNotifier)
    }

    @Test
    fun `initial state is disabled`() {
        assertThat(subject.proxySetting.value).isEqualTo(Proxy.Disabled)
        verify { mockProxyUpdateNotifier.notifyProxyChanged() }
    }

    @Test
    fun `enableProxy() - applies given proxy and proxySetting is updated`() {
        val proxy = Proxy("1.2.3.4", "515")

        subject.enableProxy(proxy)

        assertThat(subject.proxySetting.value).isEqualTo(Proxy("1.2.3.4", "515"))
        verify { mockProxyUpdateNotifier.notifyProxyChanged() }
    }

    @Test
    fun `disableProxy() - final state is disabled`() {
        subject.disableProxy()

        assertThat(subject.proxySetting.value).isEqualTo(Proxy.Disabled)

        verify { mockProxyUpdateNotifier.notifyProxyChanged() }
    }
}
