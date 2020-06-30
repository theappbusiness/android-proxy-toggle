package com.kinandcarta.create.proxytoggle.manager.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ProxyTest {

    companion object {
        private const val VALID_ADDRESS = "1.2.3.4"
        private const val VALID_PORT = 515
        private const val VALID_PROXY = "$VALID_ADDRESS:$VALID_PORT"
        private const val INVALID_PROXY = "$VALID_ADDRESS:"
    }

    @Test
    fun `from() - valid proxy`() {
        val expectedProxy = Proxy(VALID_ADDRESS, VALID_PORT)
        val result = Proxy.from(VALID_PROXY)
        assertThat(result).isEqualTo(expectedProxy)
    }

    @Test
    fun `from() - invalid proxy`() {
        val result = Proxy.from(INVALID_PROXY)
        assertThat(result).isEqualTo(Proxy.Disabled)
    }

    @Test
    fun `from() - null proxy`() {
        val result = Proxy.from(null)
        assertThat(result).isEqualTo(Proxy.Disabled)
    }

    @Test
    fun `isEnabled() - false for default Disabled proxy`() {
        val proxy = Proxy.Disabled
        assertThat(proxy.isEnabled).isFalse()
    }

    @Test
    fun `isEnabled() - false for invalid parsed proxy`() {
        val proxy = Proxy.from(INVALID_PROXY)
        assertThat(proxy.isEnabled).isFalse()
    }

    @Test
    fun `isEnabled() - true for valid parsed proxy`() {
        val proxy = Proxy.from(VALID_PROXY)
        assertThat(proxy.isEnabled).isTrue()
    }

    @Test
    fun `toSting() - returns address and port formatted`() {
        val proxy = Proxy(VALID_ADDRESS, VALID_PORT)
        assertThat(proxy.toString()).isEqualTo("$VALID_ADDRESS:$VALID_PORT")
    }

    @Test
    fun `toSting() - disabled proxy - returns address and port formatted`() {
        val proxy = Proxy.Disabled
        assertThat(proxy.toString()).isEqualTo(":0")
    }
}