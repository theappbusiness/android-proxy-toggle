package com.kinandcarta.create.proxytoggle.manager.viewmodel

import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.core.common.proxy.Proxy
import com.kinandcarta.create.proxytoggle.core.common.proxy.ProxyValidator
import com.kinandcarta.create.proxytoggle.core.common.stub.Stubs.PROXY_ADDRESS
import com.kinandcarta.create.proxytoggle.core.common.stub.Stubs.PROXY_PORT
import com.kinandcarta.create.proxytoggle.manager.R
import com.kinandcarta.create.proxytoggle.repository.appdata.AppDataRepository
import com.kinandcarta.create.proxytoggle.repository.devicesettings.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.repository.userprefs.UserPreferencesRepository
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
import kotlinx.coroutines.flow.flowOf
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
    private lateinit var mockAppDataRepository: AppDataRepository

    @RelaxedMockK
    private lateinit var mockUserPreferencesRepository: UserPreferencesRepository

    private val fakeProxyStateFlow = MutableStateFlow(Proxy.Disabled)

    private lateinit var subject: ProxyManagerViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)

        every { mockDeviceSettingsManager.proxySetting } returns fakeProxyStateFlow
        excludeRecords { mockDeviceSettingsManager.proxySetting }

        every { mockAppDataRepository.pastProxies } returns flowOf(emptyList())
        excludeRecords { mockAppDataRepository.pastProxies }

        // Emit appropriate values on fake proxy-state-flow when enabling/disabling proxy
        val proxy = slot<Proxy>()
        coEvery { mockDeviceSettingsManager.enableProxy(capture(proxy)) } answers {
            fakeProxyStateFlow.value = proxy.captured
        }
        every { mockDeviceSettingsManager.disableProxy() } answers {
            fakeProxyStateFlow.value = Proxy.Disabled
        }

        subject = initSubject()
    }

    @After
    fun tearDown() {
        confirmVerified(
            mockDeviceSettingsManager,
            mockProxyValidator,
            mockAppDataRepository
        )
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState - default is Disconnected w empty address & port and no past proxies`() {
        assertThat(subject.uiState.value).isEqualTo(
            ProxyManagerViewModel.UiState.Disconnected(
                addressState = ProxyManagerViewModel.TextFieldState(text = ""),
                portState = ProxyManagerViewModel.TextFieldState(text = ""),
                pastProxies = emptyList()
            )
        )
    }

    @Test
    fun `initial uiState - GIVEN disabled proxy & no pastProxies THEN Disconnected w empty address & port`() {
        // GIVEN
        givenPastProxies(pastProxies = emptyList())

        // WHEN
        subject = initSubject()
        dispatcher.scheduler.advanceUntilIdle()

        // THEN
        assertThat(subject.uiState.value).isEqualTo(
            ProxyManagerViewModel.UiState.Disconnected(
                addressState = ProxyManagerViewModel.TextFieldState(text = ""),
                portState = ProxyManagerViewModel.TextFieldState(text = ""),
                pastProxies = emptyList()
            )
        )
    }

    @Test
    fun `initial uiState - GIVEN disabled proxy & pastProxies THEN Disconnected w correct address & port`() {
        // GIVEN
        val pastProxy = Proxy(PROXY_ADDRESS, PROXY_PORT)
        givenPastProxies(pastProxies = listOf(pastProxy))

        // WHEN
        subject = initSubject()
        dispatcher.scheduler.advanceUntilIdle()

        // THEN
        assertThat(subject.uiState.value).isEqualTo(
            ProxyManagerViewModel.UiState.Disconnected(
                addressState = ProxyManagerViewModel.TextFieldState(text = pastProxy.address),
                portState = ProxyManagerViewModel.TextFieldState(text = pastProxy.port),
                pastProxies = listOf(pastProxy)
            )
        )
    }

    @Test
    fun `onUserInteraction(ProxyToggled) - GIVEN disabled proxy and valid address & port THEN enableProxy`() {
        // GIVEN
        every { mockProxyValidator.isValidIP(any()) } returns true
        every { mockProxyValidator.isValidPort(any()) } returns true
        givenDisconnectedWith(address = PROXY_ADDRESS, port = PROXY_PORT)

        // WHEN
        subject.onUserInteraction(ProxyManagerViewModel.UserInteraction.ToggleProxyClicked)
        dispatcher.scheduler.advanceUntilIdle()

        // THEN
        coVerify {
            mockProxyValidator.isValidIP(PROXY_ADDRESS)
            mockProxyValidator.isValidPort(PROXY_PORT)
            mockDeviceSettingsManager.enableProxy(Proxy(PROXY_ADDRESS, PROXY_PORT))
        }
        assertThat(subject.uiState.value).isEqualTo(
            ProxyManagerViewModel.UiState.Connected(
                addressState = ProxyManagerViewModel.TextFieldState(text = PROXY_ADDRESS),
                portState = ProxyManagerViewModel.TextFieldState(text = PROXY_PORT),
            )
        )
    }

    @Test
    fun `onUserInteraction(ProxyToggled) - GIVEN enabled proxy THEN disableProxy and update uiState`() {
        // GIVEN
        fakeProxyStateFlow.value = Proxy(PROXY_ADDRESS, PROXY_PORT)

        // WHEN
        subject.onUserInteraction(ProxyManagerViewModel.UserInteraction.ToggleProxyClicked)

        // THEN
        verify { mockDeviceSettingsManager.disableProxy() }
        assertThat(subject.uiState.value).isInstanceOf(ProxyManagerViewModel.UiState.Disconnected::class.java)
    }

    @Test
    fun `onUserInteraction(ProxyToggled) - GIVEN disabled proxy and invalid address THEN show address error`() {
        // GIVEN
        every { mockProxyValidator.isValidIP(PROXY_ADDRESS) } returns false
        dispatcher.scheduler.advanceUntilIdle() // so that flow of empty proxies is consumed
        givenDisconnectedWith(address = PROXY_ADDRESS, port = PROXY_PORT)

        // WHEN
        subject.onUserInteraction(ProxyManagerViewModel.UserInteraction.ToggleProxyClicked)
        dispatcher.scheduler.advanceUntilIdle()

        // THEN
        verify {
            mockProxyValidator.isValidIP(PROXY_ADDRESS)
            mockDeviceSettingsManager wasNot Called
        }
        assertThat(subject.uiState.value).isEqualTo(
            ProxyManagerViewModel.UiState.Disconnected(
                addressState = ProxyManagerViewModel.TextFieldState(
                    text = PROXY_ADDRESS,
                    error = R.string.error_invalid_address,
                    forceFocus = true
                ),
                portState = ProxyManagerViewModel.TextFieldState(text = PROXY_PORT),
                pastProxies = emptyList()
            )
        )
    }

    @Test
    fun `onUserInteraction(ProxyToggled) - GIVEN disabled proxy and invalid port THEN show port error`() {
        // GIVEN
        every { mockProxyValidator.isValidIP(PROXY_ADDRESS) } returns true
        every { mockProxyValidator.isValidPort(PROXY_PORT) } returns false
        dispatcher.scheduler.advanceUntilIdle()
        givenDisconnectedWith(address = PROXY_ADDRESS, port = PROXY_PORT)

        // WHEN
        subject.onUserInteraction(ProxyManagerViewModel.UserInteraction.ToggleProxyClicked)
        dispatcher.scheduler.advanceUntilIdle()

        // THEN
        verify {
            mockProxyValidator.isValidIP(PROXY_ADDRESS)
            mockProxyValidator.isValidPort(PROXY_PORT)
            mockDeviceSettingsManager wasNot Called
        }
        assertThat(subject.uiState.value).isEqualTo(
            ProxyManagerViewModel.UiState.Disconnected(
                addressState = ProxyManagerViewModel.TextFieldState(text = PROXY_ADDRESS),
                portState = ProxyManagerViewModel.TextFieldState(
                    text = PROXY_PORT,
                    error = R.string.error_invalid_port,
                    forceFocus = true
                ),
                pastProxies = emptyList()
            )
        )
    }

    @Test
    fun `onUserInteraction(AddressChanged) - WHEN address changes THEN update uiState`() {
        // GIVEN
        givenDisconnectedWith(address = "", port = PROXY_PORT)

        // WHEN
        subject.onUserInteraction(ProxyManagerViewModel.UserInteraction.AddressChanged(PROXY_ADDRESS))

        // THEN
        assertThat(subject.uiState.value.addressState.text).isEqualTo(PROXY_ADDRESS)
    }

    @Test
    fun `onUserInteraction(PortChanged) - WHEN port changes THEN update uiState`() {
        // GIVEN
        givenDisconnectedWith(address = PROXY_ADDRESS, port = "")

        // WHEN
        subject.onUserInteraction(ProxyManagerViewModel.UserInteraction.PortChanged(PROXY_PORT))

        // THEN
        assertThat(subject.uiState.value.portState.text).isEqualTo(PROXY_PORT)
    }

    @Test
    fun `onUserInteraction(AddressChanged) - simple filter, only digits and dots`() {
        // WHEN
        val userTyped = "125j0h111rvhiz89@$)(!V."
        subject.onUserInteraction(ProxyManagerViewModel.UserInteraction.AddressChanged(userTyped))

        // THEN
        assertThat(subject.uiState.value.addressState.text).isEqualTo(
            userTyped.filter { it.isDigit() || it == '.' }
        )
    }

    @Test
    fun `onUserInteraction(PortChanged) - simple filter, limit up to 5 digits`() {
        // WHEN
        val userTyped = "125j0hrvhiz89@$)(!V."
        subject.onUserInteraction(ProxyManagerViewModel.UserInteraction.PortChanged(userTyped))

        // THEN
        assertThat(subject.uiState.value.portState.text).isEqualTo("12508")
    }

    @Test
    fun `onUserInteraction(ThemeToggled) - delegate theme change to userPreferencesRepository`() {
        // WHEN
        subject.onUserInteraction(ProxyManagerViewModel.UserInteraction.SwitchThemeClicked)
        dispatcher.scheduler.advanceUntilIdle()

        // THEN
        coVerify { mockUserPreferencesRepository.toggleTheme() }
    }

    @Test
    fun `onForceFocusExecuted() - GIVEN any textFields have forceFocus THEN set forceFocus to false`() {
        // GIVEN
        givenDisconnectedWith(address = PROXY_ADDRESS, port = PROXY_PORT, forceFocus = true)

        // WHEN
        subject.onForceFocusExecuted()

        // THEN
        subject.uiState.value.apply {
            assertThat(addressState.forceFocus).isFalse()
            assertThat(portState.forceFocus).isFalse()
        }
    }

    private fun initSubject() = ProxyManagerViewModel(
        mockDeviceSettingsManager,
        mockProxyValidator,
        mockAppDataRepository,
        mockUserPreferencesRepository
    )

    private fun givenPastProxies(pastProxies: List<Proxy>) {
        every { mockAppDataRepository.pastProxies } returns flowOf(pastProxies)
    }

    private fun givenDisconnectedWith(
        address: String,
        port: String,
        forceFocus: Boolean = false
    ) {
        subject.getInternalUiState().value = ProxyManagerViewModel.UiState.Disconnected(
            addressState = ProxyManagerViewModel.TextFieldState(
                text = address,
                forceFocus = forceFocus
            ),
            portState = ProxyManagerViewModel.TextFieldState(
                text = port,
                forceFocus = forceFocus
            ),
            pastProxies = emptyList()
        )
    }
}
