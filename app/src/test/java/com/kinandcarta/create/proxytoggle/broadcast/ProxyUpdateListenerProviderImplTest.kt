package com.kinandcarta.create.proxytoggle.broadcast

import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.feature.widget.broadcast.WidgetProxyUpdateListener
import io.mockk.mockk
import org.junit.Test

class ProxyUpdateListenerProviderImplTest {

    private lateinit var subject: ProxyUpdateListenerProviderImpl

    @Test
    fun `getListeners() - provides widget listener`() {

        val mockListener: WidgetProxyUpdateListener = mockk()

        subject = ProxyUpdateListenerProviderImpl(mockListener)

        val result = subject.listeners

        assertThat(result.size).isEqualTo(1)
        assertThat(result.first()).isEqualTo(mockListener)
    }
}