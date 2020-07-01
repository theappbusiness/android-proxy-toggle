package com.kinandcarta.create.proxytoggle.manager.view

sealed class ProxyManagerEvent {
    object InvalidAddress : ProxyManagerEvent()
    object InvalidPort : ProxyManagerEvent()
}
