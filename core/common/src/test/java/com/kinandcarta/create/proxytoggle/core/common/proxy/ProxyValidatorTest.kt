package com.kinandcarta.create.proxytoggle.core.common.proxy

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ProxyValidatorTest {

    companion object {
        private const val ADDRESS = "1.2.3.4"
        private const val PORT = "515"

        private const val INVALID_ADDRESS = "1.2.3"
        private const val INVALID_PORT_ALPHA = "12ABC"
        private const val INVALID_PORT_MIN_RANGE = "0"
        private const val INVALID_PORT_MAX_RANGE = "99515"
        private const val EMPTY = ""
        private const val BLANK = " "
    }

    private val subject = ProxyValidator()

    @Test
    fun `isValidIP() - GIVEN a valid IP THEN return true`() {
        assertThat(subject.isValidIP(ADDRESS)).isTrue()
    }

    @Test
    fun `isValidIP() - GIVEN an invalid IP THEN return false`() {
        assertThat(subject.isValidIP(INVALID_ADDRESS)).isFalse()
    }

    @Test
    fun `isValidIP() - GIVEN an empty IP THEN return false`() {
        assertThat(subject.isValidIP(EMPTY)).isFalse()
    }

    @Test
    fun `isValidIP() - GIVEN a blank IP THEN return false`() {
        assertThat(subject.isValidIP(BLANK)).isFalse()
    }

    @Test
    fun `isValidPort() - GIVEN a valid port THEN return true`() {
        assertThat(subject.isValidPort(PORT)).isTrue()
    }

    @Test
    fun `isValidPort() - GIVEN a port lower than 1 THEN return false`() {
        assertThat(subject.isValidPort(INVALID_PORT_MIN_RANGE)).isFalse()
    }

    @Test
    fun `isValidPort() - GIVEN a port bigger than 65535 THEN return false`() {
        assertThat(subject.isValidPort(INVALID_PORT_MAX_RANGE)).isFalse()
    }

    @Test
    fun `isValidPort() - GIVEN an alphanumeric port THEN return false`() {
        assertThat(subject.isValidPort(INVALID_PORT_ALPHA)).isFalse()
    }

    @Test
    fun `isValidPort() - GIVEN an empty port THEN return false`() {
        assertThat(subject.isValidPort(EMPTY)).isFalse()
    }

    @Test
    fun `isValidPort() - GIVEN a blank port THEN return false`() {
        assertThat(subject.isValidPort(BLANK)).isFalse()
    }
}
