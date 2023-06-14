package com.kinandcarta.create.proxytoggle.core.common.proxy

import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.core.common.stub.Stubs.PROXY_ADDRESS
import com.kinandcarta.create.proxytoggle.core.common.stub.Stubs.PROXY_DISABLED
import com.kinandcarta.create.proxytoggle.core.common.stub.Stubs.PROXY_PORT
import org.junit.Test

class ProxyTest {

    @Test
    fun `isEnabled() - false for default Disabled proxy`() {
        val proxy = Proxy.Disabled
        assertThat(proxy.isEnabled).isFalse()
    }

    @Test
    fun `isEnabled() - true for valid parsed proxy`() {
        val proxy = Proxy(PROXY_ADDRESS, PROXY_PORT)
        assertThat(proxy.isEnabled).isTrue()
    }

    @Test
    fun `toSting() - returns address and port formatted`() {
        val proxy = Proxy(PROXY_ADDRESS, PROXY_PORT)
        assertThat(proxy.toString()).isEqualTo("$PROXY_ADDRESS:$PROXY_PORT")
    }

    @Test
    fun `toSting() - disabled proxy - returns address and port formatted`() {
        val proxy = Proxy.Disabled
        assertThat(proxy.toString()).isEqualTo(PROXY_DISABLED)
    }
}
