package com.kinandcarta.create.proxytoggle.android

import com.kinandcarta.create.proxytoggle.broadcast.ProxyUpdateListener
import com.kinandcarta.create.proxytoggle.broadcast.ProxyUpdateListenerProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ProxyUpdateNotifierTest {

    @MockK
    private lateinit var mockProvider: ProxyUpdateListenerProvider

    @RelaxedMockK
    private lateinit var mockListener1: ProxyUpdateListener

    @RelaxedMockK
    private lateinit var mockListener2: ProxyUpdateListener

    private lateinit var subject: ProxyUpdateNotifier

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { mockProvider.listeners } answers { listOf(mockListener1, mockListener2) }

        subject = ProxyUpdateNotifier(mockProvider)
    }

    @Test
    fun `notifyProxyChanged() - calls all listeners`() {
        subject.notifyProxyChanged()

        verify {
            mockListener1.onProxyUpdate()
            mockListener2.onProxyUpdate()
        }
    }
}
