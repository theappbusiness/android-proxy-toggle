package com.kinandcarta.create.proxytoggle.settings

import com.kinandcarta.create.proxytoggle.model.Proxy

interface AppSettings {

    var lastUsedProxy: Proxy

    var themeMode: Int
}
