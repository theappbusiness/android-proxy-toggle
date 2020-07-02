package com.kinandcarta.create.proxytoggle.model

import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.stubs.Stubs.INVALID_PROXY
import com.kinandcarta.create.proxytoggle.stubs.Stubs.PROXY_ADDRESS
import com.kinandcarta.create.proxytoggle.stubs.Stubs.PROXY_PORT
import com.kinandcarta.create.proxytoggle.stubs.Stubs.VALID_PROXY
import org.junit.Test

class ProxyMapperTest {

    private val subject = ProxyMapper()

    @Test
    fun `from() - valid proxy`() {
        val expectedProxy = Proxy(PROXY_ADDRESS, PROXY_PORT)
        val result = subject.from(VALID_PROXY)
        assertThat(result).isEqualTo(expectedProxy)
    }

    @Test
    fun `from() - invalid proxy`() {
        val result = subject.from(INVALID_PROXY)
        assertThat(result).isEqualTo(Proxy.Disabled)
    }

    @Test
    fun `from() - null proxy`() {
        val result = subject.from(null)
        assertThat(result).isEqualTo(Proxy.Disabled)
    }
}
