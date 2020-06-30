package com.kinandcarta.create.proxytoggle.manager.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ProxyMapperTest {

    companion object {
        private const val VALID_ADDRESS = "1.2.3.4"
        private const val VALID_PORT = "515"
        private const val VALID_PROXY = "$VALID_ADDRESS:$VALID_PORT"
        private const val INVALID_PROXY = "$VALID_ADDRESS:"
    }

    private val subject = ProxyMapper()

    @Test
    fun `from() - valid proxy`() {
        val expectedProxy = Proxy(VALID_ADDRESS, VALID_PORT)
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
