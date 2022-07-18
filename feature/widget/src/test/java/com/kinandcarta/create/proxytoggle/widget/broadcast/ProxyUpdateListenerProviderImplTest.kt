package com.kinandcarta.create.proxytoggle.widget.broadcast

import com.google.common.truth.Truth.assertThat
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
