package com.kinandcarta.create.proxytoggle.feature.manager.view

sealed class ProxyManagerEvent {
    object InvalidAddress : ProxyManagerEvent()
    object InvalidPort : ProxyManagerEvent()
}
