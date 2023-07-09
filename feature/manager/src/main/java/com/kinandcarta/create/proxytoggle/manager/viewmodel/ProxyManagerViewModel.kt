package com.kinandcarta.create.proxytoggle.manager.viewmodel

import android.util.Log
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinandcarta.create.proxytoggle.core.common.proxy.Proxy
import com.kinandcarta.create.proxytoggle.core.common.proxy.ProxyValidator
import com.kinandcarta.create.proxytoggle.manager.R
import com.kinandcarta.create.proxytoggle.repository.appdata.AppDataRepository
import com.kinandcarta.create.proxytoggle.repository.devicesettings.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.repository.userprefs.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class ProxyManagerViewModel @Inject constructor(
    private val deviceSettingsManager: DeviceSettingsManager,
    private val proxyValidator: ProxyValidator,
    private val appDataRepository: AppDataRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var _uiState = mutableStateOf<UiState>(
        UiState.Disconnected(
            addressState = TextFieldState(text = ""),
            portState = TextFieldState(text = ""),
            pastProxies = emptyList()
        )
    )
    val uiState: State<UiState> = _uiState

    init {
        viewModelScope.launch {
            combine(
                deviceSettingsManager.proxySetting,
                appDataRepository.pastProxies
            ) { proxy: Proxy, pastProxies: List<Proxy> ->
                Pair(proxy, pastProxies)
            }.collect { combinedValues ->
                val (proxy, pastProxies) = combinedValues
                if (proxy.isEnabled) {
                    _uiState.value = UiState.Connected(
                        addressState = TextFieldState(text = proxy.address),
                        portState = TextFieldState(text = proxy.port)
                    )
                } else {
                    val (addressText, portText) = pastProxies.firstOrNull()?.let {
                        Pair(it.address, it.port)
                    } ?: Pair("", "")
                    _uiState.value = UiState.Disconnected(
                        addressState = TextFieldState(text = addressText),
                        portState = TextFieldState(text = portText),
                        pastProxies = pastProxies
                    )
                }
            }
        }

        viewModelScope.launch {
            savedStateHandle.getStateFlow(PROXY_KEY, "").filter { it.isNotBlank() }.collect {
                Log.i("Intent", "On start intent $it")
                onSetProxy(it)
            }
        }
    }

    fun onUserInteraction(userInteraction: UserInteraction) {
        when (userInteraction) {
            UserInteraction.ToggleProxyClicked -> toggleProxy()
            UserInteraction.SwitchThemeClicked -> toggleTheme()
            is UserInteraction.AddressChanged -> onAddressChanged(userInteraction.newAddress)
            is UserInteraction.PortChanged -> onPortChanged(userInteraction.newPort)
            is UserInteraction.ProxyFromDropDownSelected -> onProxySelected(userInteraction.proxy)
        }
    }

    fun onSetProxy(proxyStr: String) {
        viewModelScope.launch {
            if (deviceSettingsManager.proxySetting.value.isEnabled) {
                // Turn off proxy before setting the proxy ip and port
                deviceSettingsManager.disableProxy()

                while (uiState.value is UiState.Connected) {
                    yield()
                }
            }
            val inputArray = proxyStr.split(':')
            val inputIp = inputArray.firstOrNull()
            val inputPort = inputArray.getOrNull(1)
            inputIp?.takeIf { it.isNotBlank() }?.let {
                onAddressChanged(it)
            }

            inputPort?.takeIf { it.isNotBlank() }?.let {
                onPortChanged(it)
            }

            // Leave it for user to turn on the proxy
        }
    }

    fun onForceFocusExecuted() {
        updateDisconnectedState {
            it.copy(
                addressState = it.addressState.copy(forceFocus = false),
                portState = it.portState.copy(forceFocus = false)
            )
        }
    }

    private fun toggleProxy() {
        if (deviceSettingsManager.proxySetting.value.isEnabled) {
            deviceSettingsManager.disableProxy()
        } else {
            enableProxyIfNoErrors()
        }
    }

    private fun toggleTheme() {
        viewModelScope.launch {
            userPreferencesRepository.toggleTheme()
        }
    }

    private fun onAddressChanged(newText: String) {
        val newTextFiltered = newText.filter { it.isDigit() || it == '.' }
        updateDisconnectedState {
            it.copy(addressState = it.addressState.copy(text = newTextFiltered))
        }
    }

    private fun onPortChanged(newText: String) {
        val newTextFiltered = newText
            .filter(Char::isDigit)
            .take(ProxyValidator.MAX_PORT.toString().length)
        updateDisconnectedState {
            it.copy(portState = it.portState.copy(text = newTextFiltered))
        }
    }

    private fun onProxySelected(proxy: Proxy) {
        updateDisconnectedState {
            UiState.Disconnected(
                addressState = TextFieldState(text = proxy.address),
                portState = TextFieldState(text = proxy.port),
                pastProxies = it.pastProxies
            )
        }
    }

    private fun enableProxyIfNoErrors() {
        updateErrors(ProxyManagerError.NoError)
        updateDisconnectedState {
            it.copy(
                addressState = it.addressState.copy(forceFocus = false),
                portState = it.portState.copy(forceFocus = false)
            )
        }

        val address = uiState.value.addressState.text
        val port = uiState.value.portState.text

        viewModelScope.launch {
            when {
                proxyValidator.isValidIP(address).not() -> {
                    delay(ERROR_DELAY)
                    updateErrors(ProxyManagerError.InvalidAddress)
                }

                proxyValidator.isValidPort(port).not() -> {
                    delay(ERROR_DELAY)
                    updateErrors(ProxyManagerError.InvalidPort)
                }

                else -> {
                    deviceSettingsManager.enableProxy(Proxy(address, port))
                }
            }
        }
    }

    private fun updateDisconnectedState(updateFunc: (UiState.Disconnected) -> UiState.Disconnected) {
        (uiState.value as? UiState.Disconnected)?.let {
            _uiState.value = updateFunc(it)
        }
    }

    private fun updateErrors(errorState: ProxyManagerError) {
        updateDisconnectedState {
            when (errorState) {
                ProxyManagerError.InvalidAddress -> {
                    it.copy(
                        addressState = it.addressState.copy(
                            error = R.string.error_invalid_address,
                            forceFocus = true
                        )
                    )
                }

                ProxyManagerError.InvalidPort -> {
                    it.copy(
                        portState = it.portState.copy(
                            error = R.string.error_invalid_port,
                            forceFocus = true
                        )
                    )
                }

                ProxyManagerError.NoError -> {
                    it.copy(
                        addressState = it.addressState.copy(error = null),
                        portState = it.portState.copy(error = null)
                    )
                }
            }
        }
    }

    @VisibleForTesting
    fun getInternalUiState(): MutableState<UiState> {
        return _uiState
    }

    sealed class UiState {
        abstract val addressState: TextFieldState
        abstract val portState: TextFieldState

        data class Connected(
            override val addressState: TextFieldState,
            override val portState: TextFieldState
        ) : UiState()

        data class Disconnected(
            override val addressState: TextFieldState,
            override val portState: TextFieldState,
            val pastProxies: List<Proxy>
        ) : UiState()
    }

    data class TextFieldState(
        val text: String,
        @StringRes val error: Int? = null,
        val forceFocus: Boolean = false
    )

    sealed class UserInteraction {
        object ToggleProxyClicked : UserInteraction()
        object SwitchThemeClicked : UserInteraction()
        data class AddressChanged(val newAddress: String) : UserInteraction()
        data class PortChanged(val newPort: String) : UserInteraction()
        data class ProxyFromDropDownSelected(val proxy: Proxy) : UserInteraction()
    }

    private sealed class ProxyManagerError {
        object InvalidAddress : ProxyManagerError()
        object InvalidPort : ProxyManagerError()
        object NoError : ProxyManagerError()
    }

    companion object {
        // NOTE: necessary delay to refocus & announce existing error on next attempt to connect!
        private const val ERROR_DELAY = 50L

        const val PROXY_KEY = "proxy"
    }
}
