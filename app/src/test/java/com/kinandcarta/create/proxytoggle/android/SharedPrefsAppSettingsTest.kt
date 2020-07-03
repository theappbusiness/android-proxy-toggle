package com.kinandcarta.create.proxytoggle.android

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.model.ProxyMapper
import com.kinandcarta.create.proxytoggle.stubs.Stubs.PROXY
import com.kinandcarta.create.proxytoggle.stubs.Stubs.VALID_PROXY
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SharedPrefsAppSettingsTest {

    @RelaxedMockK
    private lateinit var mockSharedPreferences: SharedPreferences

    @RelaxedMockK
    private lateinit var mockProxyMapper: ProxyMapper

    @RelaxedMockK
    private lateinit var mockContext: Context

    private lateinit var subject: SharedPrefsAppSettings

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { mockContext.getSharedPreferences(any(), any()) } returns mockSharedPreferences
        every { mockProxyMapper.from(VALID_PROXY) } returns PROXY

        subject = SharedPrefsAppSettings(mockContext, mockProxyMapper)
    }

    @Test
    fun `getProxy() - fetches from sharedPrefs and maps the value`() {
        every { mockSharedPreferences.getString("proxy", any()) } returns VALID_PROXY

        val result = subject.lastUsedProxy

        verify {
            mockSharedPreferences.getString("proxy", null)
            mockProxyMapper.from(VALID_PROXY)
            assertThat(result).isEqualTo(PROXY)
        }
        confirmVerified(mockProxyMapper, mockSharedPreferences)
    }

    @Test
    fun `setProxy() - stores in sharedPrefs the string version of the proxy`() {
        val string = slot<String>()
        every { mockSharedPreferences.edit { putString("proxy", capture(string)) } } returns Unit

        subject.lastUsedProxy = PROXY

        verify {
            mockSharedPreferences.edit { putString("proxy", any()) }
            assertThat(string.captured).isEqualTo(VALID_PROXY)
        }
        confirmVerified(mockProxyMapper, mockSharedPreferences)
    }
}
