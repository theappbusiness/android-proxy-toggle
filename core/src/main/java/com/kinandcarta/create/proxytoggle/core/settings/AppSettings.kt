package com.kinandcarta.create.proxytoggle.core.settings

import com.kinandcarta.create.proxytoggle.core.model.Proxy

interface AppSettings {

    var lastUsedProxy: Proxy

    var themeMode: Int
}
