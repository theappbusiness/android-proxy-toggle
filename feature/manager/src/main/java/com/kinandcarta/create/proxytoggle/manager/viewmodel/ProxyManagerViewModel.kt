package com.kinandcarta.create.proxytoggle.manager.viewmodel

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinandcarta.create.proxytoggle.core.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.core.android.ProxyValidator
import com.kinandcarta.create.proxytoggle.core.android.ThemeSwitcher
import com.kinandcarta.create.proxytoggle.core.model.Proxy
import com.kinandcarta.create.proxytoggle.core.settings.AppSettings
import com.kinandcarta.create.proxytoggle.manager.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class ProxyManagerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val deviceSettingsManager: DeviceSettingsManager,
    private val proxyValidator: ProxyValidator,
    private val appSettings: AppSettings,
    private val themeSwitcher: ThemeSwitcher
) : ViewModel() {

    private var _uiState = mutableStateOf(
        UiState(
            darkTheme = themeSwitcher.isNightMode(),
            proxyEnabled = false,
            addressState = getInitialAddressState(),
            portState = getInitialPortState()
        )
    )
    val uiState: State<UiState> = _uiState

    init {
        viewModelScope.launch {
            deviceSettingsManager.proxySetting.collect { proxy ->
                if (proxy.isEnabled) {
                    updateUiState {
                        it.copy(
                            proxyEnabled = true,
                            addressState = it.addressState.copy(text = proxy.address, error = null),
                            portState = it.portState.copy(text = proxy.port, error = null)
                        )
                    }
                } else {
                    updateUiState {
                        it.copy(proxyEnabled = false)
                    }
                }
            }
        }
    }

    fun toggleProxy() {
        if (deviceSettingsManager.proxySetting.value.isEnabled) {
            deviceSettingsManager.disableProxy()
        } else {
            enableProxy()
        }
    }

    fun toggleTheme() {
        themeSwitcher.toggleTheme()
        updateUiState { it.copy(darkTheme = themeSwitcher.isNightMode()) }
    }

    fun onAddressChanged(newText: String) {
        val newTextFiltered = newText.filter { it.isDigit() || it == '.' }
        updateUiState {
            it.copy(addressState = it.addressState.copy(text = newTextFiltered))
        }
    }

    fun onPortChanged(newText: String) {
        val newTextFiltered = newText
            .filter(Char::isDigit)
            .take(ProxyValidator.MAX_PORT.toString().length)
        updateUiState {
            it.copy(portState = it.portState.copy(text = newTextFiltered))
        }
    }

    fun onForceFocusExecuted() {
        updateUiState {
            it.copy(
                addressState = it.addressState.copy(forceFocus = false),
                portState = it.portState.copy(forceFocus = false)
            )
        }
    }

    private fun enableProxy() {
        val address = uiState.value.addressState.text
        val port = uiState.value.portState.text

        updateErrors(ProxyManagerError.NoError)
        updateUiState {
            it.copy(
                addressState = it.addressState.copy(forceFocus = false),
                portState = it.portState.copy(forceFocus = false)
            )
        }
        when {
            !proxyValidator.isValidIP(address) -> {
                viewModelScope.launch {
                    delay(ERROR_DELAY)
                    updateErrors(ProxyManagerError.InvalidAddress)
                }
            }
            !proxyValidator.isValidPort(port) -> {
                viewModelScope.launch {
                    delay(ERROR_DELAY)
                    updateErrors(ProxyManagerError.InvalidPort)
                }
            }
            else -> {
                deviceSettingsManager.enableProxy(Proxy(address, port))
            }
        }
    }

    private fun updateUiState(updateFunc: (UiState) -> UiState) {
        _uiState.value = updateFunc(_uiState.value)
    }

    private fun updateErrors(errorState: ProxyManagerError) {
        when (errorState) {
            ProxyManagerError.InvalidAddress -> {
                updateUiState {
                    it.copy(
                        addressState = it.addressState.copy(
                            error = R.string.error_invalid_address,
                            forceFocus = true
                        )
                    )
                }
            }
            ProxyManagerError.InvalidPort -> {
                updateUiState {
                    it.copy(
                        portState = it.portState.copy(
                            error = R.string.error_invalid_port,
                            forceFocus = true
                        )
                    )
                }
            }
            ProxyManagerError.NoError -> {
                updateUiState {
                    it.copy(
                        addressState = it.addressState.copy(error = null),
                        portState = it.portState.copy(error = null)
                    )
                }
            }
        }
    }

    private fun getInitialAddressState(): TextFieldState {
        return TextFieldState(
            label = context.getString(R.string.hint_ip_address),
            text = getLastUsedProxy()?.address ?: "",
            keyboardOptions = getKeyboardOptions(KeyboardType.Uri, ImeAction.Next)
        )
    }

    private fun getInitialPortState(): TextFieldState {
        return TextFieldState(
            label = context.getString(R.string.hint_port),
            text = getLastUsedProxy()?.port ?: "",
            keyboardOptions = getKeyboardOptions(KeyboardType.Number, ImeAction.Done)
        )
    }

    private fun getLastUsedProxy(): Proxy? {
        return if (appSettings.lastUsedProxy.isEnabled) appSettings.lastUsedProxy else null
    }

    private fun getKeyboardOptions(
        type: KeyboardType,
        imeAction: ImeAction
    ): KeyboardOptions {
        return KeyboardOptions.Default.copy(
            autoCorrect = false,
            keyboardType = type,
            imeAction = imeAction
        )
    }

    @VisibleForTesting
    fun getInternalUiState(): MutableState<UiState> {
        return _uiState
    }

    private sealed class ProxyManagerError {
        object InvalidAddress : ProxyManagerError()
        object InvalidPort : ProxyManagerError()
        object NoError : ProxyManagerError()
    }

    companion object {
        private const val ERROR_DELAY = 50L
    }
}
