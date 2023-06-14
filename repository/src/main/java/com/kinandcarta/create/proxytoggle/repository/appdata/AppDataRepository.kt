package com.kinandcarta.create.proxytoggle.repository.appdata

import com.kinandcarta.create.proxytoggle.core.common.proxy.Proxy
import kotlinx.coroutines.flow.Flow

interface AppDataRepository {

    val pastProxies: Flow<List<Proxy>>

    suspend fun saveProxy(proxy: Proxy)
}
