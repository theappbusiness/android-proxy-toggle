package com.kinandcarta.create.proxytoggle.tile

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.kinandcarta.create.proxytoggle.core.common.intent.getAppLaunchIntent
import com.kinandcarta.create.proxytoggle.repository.appdata.AppDataRepository
import com.kinandcarta.create.proxytoggle.repository.devicesettings.DeviceSettingsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
@AndroidEntryPoint
class ProxyTileService : TileService() {

    @Inject
    lateinit var deviceSettingsManager: DeviceSettingsManager

    @Inject
    lateinit var appDataRepository: AppDataRepository

    private var coroutineJob: Job? = null

    override fun onClick() {
        toggleProxy()
    }

    override fun onStartListening() {
        updateTile()
    }

    override fun onStopListening() {
        coroutineJob?.cancel()
    }

    private fun toggleProxy() {
        coroutineJob = MainScope().launch {
            val proxy = deviceSettingsManager.proxySetting.value
            if (proxy.isEnabled) {
                deviceSettingsManager.disableProxy()
            } else {
                val lastUsedProxy = appDataRepository.pastProxies.first().firstOrNull()
                if (lastUsedProxy != null) {
                    deviceSettingsManager.enableProxy(lastUsedProxy)
                } else {
                    // There is no last used Proxy, prompt the user to create one
                    getAppLaunchIntent(baseContext)?.let { startActivityAndCollapse(it) }
                }
            }
            updateTile()
        }
    }

    private fun updateTile() {
        val proxy = deviceSettingsManager.proxySetting.value
        if (proxy.isEnabled) {
            qsTile.apply {
                label = proxy.toString()
                state = Tile.STATE_ACTIVE
                updateTile()
            }
        } else {
            qsTile.apply {
                label = getString(R.string.no_proxy_tile)
                state = Tile.STATE_INACTIVE
                updateTile()
            }
        }
    }
}
