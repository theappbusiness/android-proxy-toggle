package com.kinandcarta.create.proxytoggle.manager.settings

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.manager.model.Proxy
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class DeviceSettingsManagerTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit var subject: DeviceSettingsManager

    @Before
    fun setUp() {
        subject = DeviceSettingsManager(context)
    }

    @Test
    fun `initial state is disabled`() {
        assertThat(subject.proxySetting.value).isEqualTo(Proxy.Disabled)
    }

    @Test
    fun `enableProxy() - applies given proxy and proxySetting is updated`() {
        val proxy = Proxy(address = "1.2.3.4", port = 515)

        subject.enableProxy(proxy)

        assertThat(subject.proxySetting.value).isEqualTo(Proxy(address = "1.2.3.4", port = 515))
    }

    @Test
    fun `disableProxy() - final state is disabled`() {
        subject.disableProxy()

        assertThat(subject.proxySetting.value).isEqualTo(Proxy.Disabled)
    }
}
