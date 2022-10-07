package com.kinandcarta.create.proxytoggle.manager.viewmodel

import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.core.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.core.android.ProxyValidator
import com.kinandcarta.create.proxytoggle.core.android.ThemeSwitcher
import com.kinandcarta.create.proxytoggle.core.model.Proxy
import com.kinandcarta.create.proxytoggle.core.settings.AppSettings
import com.kinandcarta.create.proxytoggle.core.stub.Stubs.PROXY_ADDRESS
import com.kinandcarta.create.proxytoggle.core.stub.Stubs.PROXY_PORT
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerViewModel.UiState.TextFieldState
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerViewModel.UserInteraction
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.excludeRecords
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProxyManagerViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var mockDeviceSettingsManager: DeviceSettingsManager

    @MockK
    private lateinit var mockProxyValidator: ProxyValidator

    @MockK
    private lateinit var mockAppSettings: AppSettings

    @RelaxedMockK
    private lateinit var mockThemeSwitcher: ThemeSwitcher

    private val fakeProxyStateFlow = MutableStateFlow(Proxy.Disabled)

    private lateinit var subject: ProxyManagerViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)

        every { mockDeviceSettingsManager.proxySetting } returns fakeProxyStateFlow
        excludeRecords { mockDeviceSettingsManager.proxySetting }

        every { mockAppSettings.lastUsedProxy } returns Proxy.Disabled
        excludeRecords { mockAppSettings.lastUsedProxy }

        // Mock StateFlow emitter
        val proxy = slot<Proxy>()
        every { mockDeviceSettingsManager.enableProxy(capture(proxy)) } answers {
            fakeProxyStateFlow.value = proxy.captured
        }
        every { mockDeviceSettingsManager.disableProxy() } answers {
            fakeProxyStateFlow.value = Proxy.Disabled
        }

        every { mockThemeSwitcher.isNightMode() } returns true

        subject = initSubject()
    }

    @After
    fun tearDown() {
        confirmVerified(
            mockDeviceSettingsManager,
            mockProxyValidator,
            mockAppSettings,
            mockThemeSwitcher
        )
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState - GIVEN isNightMode is true THEN darkTheme is true`() {
        verify { mockThemeSwitcher.isNightMode() }

        assertThat(subject.uiState.value.darkTheme).isTrue()
    }

    @Test
    fun `initial uiState - GIVEN isNightMode is false THEN darkTheme is false`() {
        // GIVEN
        every { mockThemeSwitcher.isNightMode() } returns false

        // WHEN
        subject = initSubject()

        // THEN
        verify { mockThemeSwitcher.isNightMode() }

        assertThat(subject.uiState.value.darkTheme).isFalse()
    }

    @Test
    fun `initial uiState - address and port have no errors and no forceFocus`() {
        verify { mockThemeSwitcher.isNightMode() }

        subject.uiState.value.addressState.apply {
            assertThat(error).isNull()
            assertThat(forceFocus).isFalse()
        }
        subject.uiState.value.portState.apply {
            assertThat(error).isNull()
            assertThat(forceFocus).isFalse()
        }
    }

    @Test
    fun `initial uiState - GIVEN no last used proxy THEN address and port are empty`() {
        verify { mockThemeSwitcher.isNightMode() }

        subject.uiState.value.apply {
            assertThat(addressState.text).isEmpty()
            assertThat(portState.text).isEmpty()
        }
    }

    @Test
    fun `initial uiState - GIVEN last used proxy exists THEN address and port have correct texts`() {
        // GIVEN
        every { mockAppSettings.lastUsedProxy } returns Proxy(PROXY_ADDRESS, PROXY_PORT)

        // WHEN
        subject = initSubject()

        // THEN
        verify { mockThemeSwitcher.isNightMode() }

        subject.uiState.value.apply {
            assertThat(addressState.text).isEqualTo(PROXY_ADDRESS)
            assertThat(portState.text).isEqualTo(PROXY_PORT)
        }
    }

    @Test
    fun `onUserInteraction(ProxyToggled) - GIVEN disabled proxy THEN call DeviceSettingsManager update uiState`() {
        // GIVEN
        every { mockProxyValidator.isValidIP(any()) } returns true
        every { mockProxyValidator.isValidPort(any()) } returns true
        givenTextFieldValues(PROXY_ADDRESS, PROXY_PORT)

        // WHEN
        subject.onUserInteraction(UserInteraction.ProxyToggled)

        // THEN
        verify {
            mockThemeSwitcher.isNightMode()
            mockProxyValidator.isValidIP(PROXY_ADDRESS)
            mockProxyValidator.isValidPort(PROXY_PORT)
            mockDeviceSettingsManager.enableProxy(
                Proxy(PROXY_ADDRESS, PROXY_PORT)
            )
        }

        dispatcher.scheduler.advanceUntilIdle()
        subject.uiState.value.apply {
            assertThat(proxyEnabled).isEqualTo(true)
            assertThat(addressState.text).isEqualTo(PROXY_ADDRESS)
            assertThat(portState.text).isEqualTo(PROXY_PORT)
        }
    }

    @Test
    fun `onUserInteraction(ProxyToggled) - GIVEN enabled proxy THEN call DeviceSettingsManager and update uiState`() {
        // GIVEN
        fakeProxyStateFlow.value = Proxy(PROXY_ADDRESS, PROXY_PORT)

        // WHEN
        subject.onUserInteraction(UserInteraction.ProxyToggled)

        // THEN
        verify {
            mockThemeSwitcher.isNightMode()
            mockDeviceSettingsManager.disableProxy()
        }

        dispatcher.scheduler.advanceUntilIdle()
        assertThat(subject.uiState.value.proxyEnabled).isEqualTo(false)
    }

    @Test
    fun `onUserInteraction(ProxyToggled) - GIVEN disabled proxy and invalid address THEN show address error`() {
        // GIVEN
        every { mockProxyValidator.isValidIP(PROXY_ADDRESS) } returns false
        givenTextFieldValues(PROXY_ADDRESS, PROXY_PORT)

        // WHEN
        subject.onUserInteraction(UserInteraction.ProxyToggled)

        // THEN
        verify {
            mockThemeSwitcher.isNightMode()
            mockProxyValidator.isValidIP(PROXY_ADDRESS)
            mockDeviceSettingsManager wasNot Called
        }

        dispatcher.scheduler.advanceUntilIdle()
        subject.uiState.value.apply {
            assertThat(proxyEnabled).isEqualTo(false)
            assertThat(addressState.error).isNotNull()
            assertThat(addressState.forceFocus).isTrue()
            assertThat(portState.error).isNull()
            assertThat(portState.forceFocus).isFalse()
        }
    }

    @Test
    fun `onUserInteraction(ProxyToggled) - GIVEN disabled proxy and invalid port THEN show port error`() {
        // GIVEN
        every { mockProxyValidator.isValidIP(PROXY_ADDRESS) } returns true
        every { mockProxyValidator.isValidPort(PROXY_PORT) } returns false
        givenTextFieldValues(PROXY_ADDRESS, PROXY_PORT)

        // WHEN
        subject.onUserInteraction(UserInteraction.ProxyToggled)

        // THEN
        verify {
            mockThemeSwitcher.isNightMode()
            mockProxyValidator.isValidIP(PROXY_ADDRESS)
            mockProxyValidator.isValidPort(PROXY_PORT)
            mockDeviceSettingsManager wasNot Called
        }

        dispatcher.scheduler.advanceUntilIdle()
        subject.uiState.value.apply {
            assertThat(proxyEnabled).isEqualTo(false)
            assertThat(addressState.error).isNull()
            assertThat(addressState.forceFocus).isFalse()
            assertThat(portState.error).isNotNull()
            assertThat(portState.forceFocus).isTrue()
        }
    }

    @Test
    fun `onUserInteraction(AddressChanged) - WHEN address changes THEN update uiState`() {
        // GIVEN
        givenTextFieldValues("", PROXY_PORT)

        // WHEN
        subject.onUserInteraction(UserInteraction.AddressChanged(PROXY_ADDRESS))

        // THEN
        verify { mockThemeSwitcher.isNightMode() }
        assertThat(subject.uiState.value.addressState.text).isEqualTo(PROXY_ADDRESS)
    }

    @Test
    fun `onUserInteraction(PortChanged) - WHEN port changes THEN update uiState`() {
        // GIVEN
        givenTextFieldValues(PROXY_ADDRESS, "")

        // WHEN
        subject.onUserInteraction(UserInteraction.PortChanged(PROXY_PORT))

        // THEN
        verify { mockThemeSwitcher.isNightMode() }
        assertThat(subject.uiState.value.portState.text).isEqualTo(PROXY_PORT)
    }

    @Test
    fun `onUserInteraction(AddressChanged) - simple filter, only digits and dots`() {
        // WHEN
        val userTyped = "125j0h111rvhiz89@$)(!V."
        subject.onUserInteraction(UserInteraction.AddressChanged(userTyped))

        // THEN
        verify { mockThemeSwitcher.isNightMode() }
        assertThat(subject.uiState.value.addressState.text).isEqualTo(
            userTyped.filter { it.isDigit() || it == '.' }
        )
    }

    @Test
    fun `onUserInteraction(PortChanged) - simple filter, only up to 5 digits`() {
        // WHEN
        val userTyped = "125j0hrvhiz89@$)(!V."
        subject.onUserInteraction(UserInteraction.PortChanged(userTyped))

        // THEN
        verify { mockThemeSwitcher.isNightMode() }
        assertThat(subject.uiState.value.portState.text).isEqualTo("12508")
    }

    @Test
    fun `onForceFocusExecuted() - GIVEN any textFields have forceFocus THEN set forceFocus to false`() {
        // GIVEN
        givenTextFieldValues(PROXY_ADDRESS, PROXY_PORT, forceFocus = true)

        // WHEN
        subject.onForceFocusExecuted()

        // THEN
        verify { mockThemeSwitcher.isNightMode() }

        subject.uiState.value.apply {
            assertThat(addressState.forceFocus).isFalse()
            assertThat(portState.forceFocus).isFalse()
        }
    }

    @Test
    fun `ThemeToggled - delegate theme change to themeSwitcher`() {
        // WHEN
        subject.onUserInteraction(UserInteraction.ThemeToggled)

        // THEN
        verify {
            mockThemeSwitcher.isNightMode()
            mockThemeSwitcher.toggleTheme()
        }
    }

    private fun initSubject() = ProxyManagerViewModel(
        mockDeviceSettingsManager,
        mockProxyValidator,
        mockAppSettings,
        mockThemeSwitcher
    )

    private fun givenTextFieldValues(address: String, port: String, forceFocus: Boolean = false) {
        subject.getInternalUiState().value = subject.uiState.value.copy(
            addressState = TextFieldState(
                text = address,
                forceFocus = forceFocus
            ),
            portState = TextFieldState(
                text = port,
                forceFocus = forceFocus
            )
        )
    }
}
