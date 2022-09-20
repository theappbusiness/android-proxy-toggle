package com.kinandcarta.create.proxytoggle.manager.viewmodel

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions

data class TextFieldState(
    val label: String,
    val text: String,
    val keyboardOptions: KeyboardOptions,
    @StringRes val error: Int? = null,
    val forceFocus: Boolean = false
)
