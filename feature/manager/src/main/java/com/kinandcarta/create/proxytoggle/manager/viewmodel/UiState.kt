package com.kinandcarta.create.proxytoggle.manager.viewmodel

data class UiState(
    val darkTheme: Boolean,
    val proxyEnabled: Boolean,
    val addressState: TextFieldState,
    val portState: TextFieldState
)
