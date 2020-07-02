package com.kinandcarta.create.proxytoggle.feature.tile

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.kinandcarta.create.proxytoggle.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.model.Proxy
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
@AndroidEntryPoint
class ProxyTileService : TileService() {

    @Inject
    lateinit var deviceSettingsManager: DeviceSettingsManager

    override fun onClick() {
        toggleProxy()
    }

    override fun onStartListening() {
        updateTile()
    }

    private fun toggleProxy() {
        val proxy = deviceSettingsManager.proxySetting.value ?: Proxy.Disabled
        if (proxy.isEnabled) {
            deviceSettingsManager.disableProxy()
        } else {
            // TODO Take this from SharedPrefs once the user is able to input
            deviceSettingsManager.enableProxy(Proxy("192.168.1.215", "8888"))
        }
        updateTile()
    }

    private fun updateTile() {
        val proxy = deviceSettingsManager.proxySetting.value ?: Proxy.Disabled
        if (proxy.isEnabled) {
            qsTile.apply {
                label = proxy.toString()
                state = Tile.STATE_ACTIVE
                updateTile()
            }
        } else {
            qsTile.apply {
                label = "Proxy Disabled"
                state = Tile.STATE_INACTIVE
                updateTile()
            }
        }
    }
}
