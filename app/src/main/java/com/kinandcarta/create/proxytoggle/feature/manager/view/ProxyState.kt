package com.kinandcarta.create.proxytoggle.feature.manager.view

sealed class ProxyState {
    data class Enabled(val address: String, val port: String) : ProxyState()
    data class Disabled(val errorMessage: String? = null) : ProxyState()
}
