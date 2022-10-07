@file:Suppress("TooManyFunctions")

package com.kinandcarta.create.proxytoggle.manager.view.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import com.kinandcarta.create.proxytoggle.core.theme.InputTextStyle
import com.kinandcarta.create.proxytoggle.core.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.manager.R
import com.kinandcarta.create.proxytoggle.manager.view.extension.ModifierExt.shiftFocusToNextOnTabModifier
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerViewModel.UiState.TextFieldState

@Suppress("LongParameterList")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProxyToggleTextField(
    label: String,
    state: TextFieldState,
    onTextChanged: (String) -> Unit,
    enabled: Boolean,
    keyboardOptions: KeyboardOptions,
    onForceFocusExecuted: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }
    if (state.forceFocus) {
        focusRequester.requestFocus()
        onForceFocusExecuted()
    }

    val focusManager = LocalFocusManager.current

    Column {
        OutlinedTextField(
            value = state.text,
            onValueChange = onTextChanged,
            enabled = enabled,
            modifier = Modifier
                .focusRequester(focusRequester)
                .shiftFocusToNextOnTabModifier(focusManager),
            textStyle = InputTextStyle,
            label = { Text(label) },
            trailingIcon = state.error?.let {
                {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = null
                    )
                }
            },
            isError = state.error != null,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            singleLine = true
        )
        Text(
            text = state.error?.let { stringResource(it) } ?: "",
            modifier = Modifier
                .clearAndSetSemantics {}
                .padding(start = dimensionResource(R.dimen.textfield_error_padding)),
            style = MaterialTheme.typography.caption.copy(
                color = MaterialTheme.colors.error
            )
        )
    }
}

@Preview(name = "Enabled No Text (Light)", group = "ProxyToggleTextField")
@Composable
fun ProxyToggleTextFieldEnabledNoTextPreview() {
    ProxyToggleTextFieldPreviewContent()
}

@Preview(name = "Enabled No Text (Dark)", group = "ProxyToggleTextField")
@Composable
fun ProxyToggleTextFieldEnabledNoTextPreviewDark() {
    ProxyToggleTextFieldPreviewContent(darkTheme = true)
}

@Preview(name = "Enabled With Text (Light)", group = "ProxyToggleTextField")
@Composable
fun ProxyToggleTextFieldEnabledWithTextPreview() {
    ProxyToggleTextFieldPreviewContent(text = "192.168.1.1")
}

@Preview(name = "Enabled With Text (Dark)", group = "ProxyToggleTextField")
@Composable
fun ProxyToggleTextFieldEnabledWithTextPreviewDark() {
    ProxyToggleTextFieldPreviewContent(darkTheme = true, text = "192.168.1.1")
}

@Preview(name = "Error (Light)", group = "ProxyToggleTextField")
@Composable
fun ProxyToggleTextFieldErrorTextPreview() {
    ProxyToggleTextFieldPreviewContent(
        text = "foobar",
        error = R.string.error_invalid_address
    )
}

@Preview(name = "Error (Dark)", group = "ProxyToggleTextField")
@Composable
fun ProxyToggleTextFieldErrorPreviewDark() {
    ProxyToggleTextFieldPreviewContent(
        darkTheme = true,
        text = "foobar",
        error = R.string.error_invalid_address
    )
}

@Preview(name = "Disabled (Light)", group = "ProxyToggleTextField")
@Composable
fun ProxyToggleTextFieldDisabledPreview() {
    ProxyToggleTextFieldPreviewContent(text = "192.168.1.1", enabled = false)
}

@Preview(name = "Disabled (Dark)", group = "ProxyToggleTextField")
@Composable
fun ProxyToggleTextFieldDisabledPreviewDark() {
    ProxyToggleTextFieldPreviewContent(darkTheme = false, text = "192.168.1.1", enabled = false)
}

@Composable
private fun ProxyToggleTextFieldPreviewContent(
    darkTheme: Boolean = false,
    text: String = "",
    error: Int? = null,
    enabled: Boolean = true
) {
    ProxyToggleTheme(darkTheme = darkTheme) {
        Surface {
            ProxyToggleTextField(
                label = stringResource(R.string.hint_ip_address),
                state = TextFieldState(
                    text = text,
                    error = error
                ),
                onTextChanged = {},
                enabled = enabled,
                keyboardOptions = KeyboardOptions.Default,
                onForceFocusExecuted = {}
            )
        }
    }
}
