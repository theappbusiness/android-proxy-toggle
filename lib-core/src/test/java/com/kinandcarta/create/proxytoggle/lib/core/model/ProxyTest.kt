package com.kinandcarta.create.proxytoggle.lib.core.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ProxyTest {

    companion object {
        private const val VALID_ADDRESS = "1.2.3.4"
        private const val VALID_PORT = "515"
    }

    @Test
    fun `isEnabled() - false for default Disabled proxy`() {
        val proxy = Proxy.Disabled
        assertThat(proxy.isEnabled).isFalse()
    }

    @Test
    fun `isEnabled() - true for valid parsed proxy`() {
        val proxy = Proxy(VALID_ADDRESS, VALID_PORT)
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
