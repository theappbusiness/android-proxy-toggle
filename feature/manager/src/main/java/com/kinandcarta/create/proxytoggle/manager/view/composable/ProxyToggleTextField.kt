@file:Suppress("TooManyFunctions")

package com.kinandcarta.create.proxytoggle.manager.view.composable

import android.view.KeyEvent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.kinandcarta.create.proxytoggle.core.theme.InputTextStyle
import com.kinandcarta.create.proxytoggle.core.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.manager.R

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun ProxyToggleTextField(
    texts: TextFieldTexts,
    onTextChanged: (String) -> Unit,
    enabled: Boolean,
    keyboardOptions: KeyboardOptions,
    lastClickTime: Long
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var hasFocus = false

    fun Modifier.saveFocusModifier() = this
        .focusRequester(focusRequester)
        .onFocusChanged { hasFocus = it.isFocused }

    val focusManager = LocalFocusManager.current
    fun Modifier.shiftFocusToNextOnTabModifier() = this.onPreviewKeyEvent {
        if (it.key == Key.Tab && it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
            focusManager.moveFocus(FocusDirection.Next)
            true
        } else {
            false
        }
    }

    Column {
        OutlinedTextField(
            value = texts.text,
            onValueChange = onTextChanged,
            enabled = enabled,
            modifier = Modifier
                .saveFocusModifier()
                .shiftFocusToNextOnTabModifier(),
            textStyle = InputTextStyle,
            label = { Text(texts.label) },
            trailingIcon = texts.errorText?.let {
                {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colors.error
                    )
                }
            },
            isError = texts.errorText != null,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            singleLine = true
        )
        Text(
            text = texts.errorText ?: "",
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.textfield_error_padding))
                .alpha(if (texts.errorText != null) 1f else 0f),
            style = MaterialTheme.typography.caption.copy(
                color = MaterialTheme.colors.error
            )
        )
    }
    LaunchedEffect(lastClickTime) {
        if (hasFocus.not() && texts.errorText != null) {
            focusRequester.requestFocus()
        }
    }
}

fun getKeyboardOptions(
    type: KeyboardType,
    imeAction: ImeAction
): KeyboardOptions {
    return KeyboardOptions.Default.copy(
        autoCorrect = false,
        keyboardType = type,
        imeAction = imeAction
    )
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
        error = stringResource(R.string.error_invalid_address)
    )
}

@Preview(name = "Error (Dark)", group = "ProxyToggleTextField")
@Composable
fun ProxyToggleTextFieldErrorPreviewDark() {
    ProxyToggleTextFieldPreviewContent(
        darkTheme = true,
        text = "foobar",
        error = stringResource(R.string.error_invalid_address)
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
    error: String? = null,
    enabled: Boolean = true
) {
    ProxyToggleTheme(darkTheme = darkTheme, isPreview = true) {
        Surface {
            ProxyToggleTextField(
                texts = TextFieldTexts(text, stringResource(R.string.hint_ip_address), error),
                onTextChanged = { },
                enabled = enabled,
                keyboardOptions = getKeyboardOptions(KeyboardType.Uri, ImeAction.Default),
                lastClickTime = 0
            )
        }
    }
}
