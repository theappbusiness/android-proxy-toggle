package com.kinandcarta.create.proxytoggle.core.common.proxyupdate

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ProxyUpdateNotifierTest {

    @RelaxedMockK
    private lateinit var mockListener1: ProxyUpdateListener

    @RelaxedMockK
    private lateinit var mockListener2: ProxyUpdateListener

    private lateinit var subject: ProxyUpdateNotifier

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        subject = ProxyUpdateNotifier(setOf(mockListener1, mockListener2))
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
