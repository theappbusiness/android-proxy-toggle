package com.kinandcarta.create.proxytoggle.repository.proxymapper

import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.core.common.proxy.Proxy
import com.kinandcarta.create.proxytoggle.core.common.stub.Stubs.INVALID_PROXY_ADDRESS
import com.kinandcarta.create.proxytoggle.core.common.stub.Stubs.INVALID_PROXY_PORT
import com.kinandcarta.create.proxytoggle.core.common.stub.Stubs.PROXY
import com.kinandcarta.create.proxytoggle.core.common.stub.Stubs.VALID_PROXY
import org.junit.Test

class ProxyMapperTest {

    private val subject = ProxyMapper()

    @Test
    fun `from() - valid proxy`() {
        val result = subject.from(VALID_PROXY)
        assertThat(result).isEqualTo(PROXY)
    }

    @Test
    fun `from() - invalid proxy port`() {
        val result = subject.from(INVALID_PROXY_PORT)
        assertThat(result).isEqualTo(Proxy.Disabled)
    }

    @Test
    fun `from() - invalid proxy address`() {
        val result = subject.from(INVALID_PROXY_ADDRESS)
        assertThat(result).isEqualTo(Proxy.Disabled)
    }

    @Test
    fun `from() - null proxy`() {
        val result = subject.from(null)
        assertThat(result).isEqualTo(Proxy.Disabled)
    }
}
