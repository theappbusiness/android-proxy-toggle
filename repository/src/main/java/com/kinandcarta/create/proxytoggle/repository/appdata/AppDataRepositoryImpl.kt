package com.kinandcarta.create.proxytoggle.repository.appdata

import androidx.datastore.core.DataStore
import com.kinandcarta.create.proxytoggle.core.common.proxy.Proxy
import com.kinandcarta.create.proxytoggle.datastore.AppData
import com.kinandcarta.create.proxytoggle.datastore.PastProxy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<AppData>
) : AppDataRepository {

    override val pastProxies: Flow<List<Proxy>> by lazy {
        appData.map { appData ->
            appData.pastProxiesList
                .sortedByDescending { it.timestamp }
                .map { Proxy(it.address, it.port) }
        }.distinctUntilChanged()
    }

    override suspend fun saveProxy(proxy: Proxy) {
        dataStore.updateData { appData ->
            val proxiesToKeep = appData.pastProxiesList.filter {
                it.address != proxy.address || it.port != proxy.port
            }.take(MAX_SAVED_PROXIES - 1).toMutableList()
            val proxyToAdd = PastProxy.newBuilder()
                .setAddress(proxy.address)
                .setPort(proxy.port)
                .setTimestamp(Instant.now().epochSecond)
                .build()
            proxiesToKeep.add(0, proxyToAdd)
            appData.toBuilder().clearPastProxies().addAllPastProxies(proxiesToKeep).build()
        }
    }

    private val appData: Flow<AppData> by lazy {
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(AppData.getDefaultInstance())
            } else {
                throw exception
            }
        }
    }

    companion object {
        private const val MAX_SAVED_PROXIES = 5
    }
}
