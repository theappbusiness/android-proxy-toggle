@file:Suppress("TooManyFunctions")

package com.kinandcarta.create.proxytoggle.manager.view.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kinandcarta.create.proxytoggle.core.ui.theme.InputTextStyle
import com.kinandcarta.create.proxytoggle.core.ui.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.manager.R
import com.kinandcarta.create.proxytoggle.manager.view.extension.ModifierExt.shiftFocusToNextOnTabModifier
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerViewModel

@Suppress("LongParameterList")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProxyToggleTextField(
    label: String,
    state: ProxyManagerViewModel.TextFieldState,
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
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            supportingText = {
                Text(
                    text = state.error?.let { stringResource(it) } ?: "",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.error
                    )
                )
            },
            isError = state.error != null,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            singleLine = true
        )
    }
}

private class PreviewStateParameterProvider :
    PreviewParameterProvider<Pair<ProxyManagerViewModel.TextFieldState, Boolean>> {

    override val values = sequenceOf(
        Pair(ProxyManagerViewModel.TextFieldState(text = ""), true),
        Pair(ProxyManagerViewModel.TextFieldState(text = "192.168.1.1"), true),
        Pair(
            ProxyManagerViewModel.TextFieldState(
                text = "192.168",
                error = R.string.error_invalid_address
            ),
            true
        ),
        Pair(ProxyManagerViewModel.TextFieldState(text = "192.168.1.1"), false),
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
@ShowkaseComposable(skip = true)
fun ProxyToggleTextFieldEnabledNoTextPreview(
    @PreviewParameter(PreviewStateParameterProvider::class) state: Pair<ProxyManagerViewModel.TextFieldState, Boolean>
) {
    ProxyToggleTextFieldPreviewContent(
        textState = state.first,
        enabled = state.second
    )
}

@Composable
private fun ProxyToggleTextFieldPreviewContent(
    textState: ProxyManagerViewModel.TextFieldState,
    enabled: Boolean
) {
    ProxyToggleTheme {
        Surface {
            ProxyToggleTextField(
                label = stringResource(R.string.hint_ip_address),
                state = textState,
                onTextChanged = {},
                enabled = enabled,
                keyboardOptions = KeyboardOptions.Default,
                onForceFocusExecuted = {}
            )
        }
    }
}
