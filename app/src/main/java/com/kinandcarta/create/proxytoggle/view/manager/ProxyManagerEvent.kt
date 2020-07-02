package com.kinandcarta.create.proxytoggle.view.manager

sealed class ProxyManagerEvent {
    object InvalidAddress : ProxyManagerEvent()
    object InvalidPort : ProxyManagerEvent()
}
