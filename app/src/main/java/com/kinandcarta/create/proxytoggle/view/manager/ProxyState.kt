package com.kinandcarta.create.proxytoggle.view.manager

sealed class ProxyState {
    data class Enabled(val address: String, val port: String) : ProxyState()
    data class Disabled(val errorMessage: String? = null) : ProxyState()
}
