package com.kinandcarta.create.proxytoggle.manager.mapper

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.core.model.Proxy
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerEvent
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyState
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProxyUiDataMapperTest {

    private val context = ApplicationProvider.getApplicationContext<Application>()

    private val subject = ProxyUiDataMapper(context)

    @Test
    fun `getUserInputErrors() - WHEN InvalidAddress THEN correct addressError AND null portError`() {
        val inputErrors = subject.getUserInputErrors(ProxyManagerEvent.InvalidAddress)
        assertThat(inputErrors.addressError).isEqualTo("Please enter a valid IP address")
        assertThat(inputErrors.portError).isNull()
    }

    @Test
    fun `getUserInputErrors() - WHEN InvalidPort THEN correct portError AND null addressError`() {
        val inputErrors = subject.getUserInputErrors(ProxyManagerEvent.InvalidPort)
        assertThat(inputErrors.addressError).isNull()
        assertThat(inputErrors.portError).isEqualTo("Please enter a valid port")
    }

    @Test
    fun `getUserInputErrors() - WHEN NoError THEN both addressError and portError are null`() {
        val inputErrors = subject.getUserInputErrors(ProxyManagerEvent.NoError)
        assertThat(inputErrors.addressError).isNull()
        assertThat(inputErrors.portError).isNull()
    }

    @Test
    fun `getActiveProxyData() - WHEN active proxy is Enabled THEN get its data as AddressAndPort`() {
        val proxyState = ProxyState.Enabled("192.168.1.1", "8888")
        val addressAndPort = subject.getActiveProxyData(proxyState)
        assertThat(addressAndPort).isNotNull()
        assertThat(addressAndPort!!.address).isEqualTo(proxyState.address)
        assertThat(addressAndPort.port).isEqualTo(proxyState.port)
    }

    @Test
    fun `getActiveProxyData() - WHEN active proxy is Disabled THEN get null instead of AddressAndPort`() {
        val proxyState = ProxyState.Disabled()
        val addressAndPort = subject.getActiveProxyData(proxyState)
        assertThat(addressAndPort).isNull()
    }

    @Test
    fun `getActiveProxyData() - WHEN active proxy is null THEN get null instead of AddressAndPort`() {
        val addressAndPort = subject.getActiveProxyData(null)
        assertThat(addressAndPort).isNull()
    }

    @Test
    fun `getLastProxyUsedData() - WHEN last proxy used exists THEN get its data as AddressAndPort`() {
        val lastProxyUsed = Proxy("192.168.1.1", "8888")
        val addressAndPort = subject.getLastProxyUsedData(lastProxyUsed)
        assertThat(addressAndPort).isNotNull()
        assertThat(addressAndPort!!.address).isEqualTo(lastProxyUsed.address)
        assertThat(addressAndPort.port).isEqualTo(lastProxyUsed.port)
    }

    @Test
    fun `getLastProxyUsedData() - WHEN last proxy used does not exist THEN get null instead of AddressAndPort`() {
        val lastProxyUsed = Proxy.Disabled
        val addressAndPort = subject.getLastProxyUsedData(lastProxyUsed)
        assertThat(addressAndPort).isNull()
    }
}
