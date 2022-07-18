package com.kinandcarta.create.proxytoggle.manager.viewmodel

sealed class ProxyManagerEvent {
    object InvalidAddress : ProxyManagerEvent()
    object InvalidPort : ProxyManagerEvent()
    object NoError : ProxyManagerEvent()
}
