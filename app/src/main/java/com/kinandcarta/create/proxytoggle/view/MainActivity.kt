package com.kinandcarta.create.proxytoggle.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kinandcarta.create.proxytoggle.core.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.manager.mapper.ProxyUiDataMapper
import com.kinandcarta.create.proxytoggle.manager.view.composable.BlockAppScreen
import com.kinandcarta.create.proxytoggle.manager.view.composable.ProxyManagerScreen
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var proxyUiDataMapper: ProxyUiDataMapper

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val heightSizeClass = calculateWindowSizeClass(this).heightSizeClass
            val useVerticalLayout = heightSizeClass != WindowHeightSizeClass.Compact

            val viewModel: ProxyManagerViewModel = viewModel()

            val darkTheme by viewModel.isNightMode.collectAsState()
            val proxyEvent by viewModel.proxyEvent.observeAsState()
            val proxyState by viewModel.proxyState.observeAsState()

            val inputErrors = proxyUiDataMapper.getUserInputErrors(proxyEvent)
            val activeProxyData = proxyUiDataMapper.getActiveProxyData(proxyState)
            val lastProxyUsedData = proxyUiDataMapper.getLastProxyUsedData(viewModel.lastUsedProxy)

            ProxyToggleTheme(darkTheme = darkTheme) {
                when (
                    ContextCompat.checkSelfPermission(
                        LocalContext.current,
                        Manifest.permission.WRITE_SECURE_SETTINGS
                    )
                ) {
                    PackageManager.PERMISSION_DENIED -> BlockAppScreen()
                    PackageManager.PERMISSION_GRANTED -> {
                        ProxyManagerScreen(
                            useVerticalLayout = useVerticalLayout,
                            activeProxyData = activeProxyData,
                            lastProxyUsedData = lastProxyUsedData,
                            inputErrors = inputErrors,
                            onToggleTheme = { viewModel.toggleTheme() },
                            onToggleProxy = { ipAddress, port ->
                                if (activeProxyData != null) {
                                    viewModel.disableProxy()
                                } else {
                                    viewModel.enableProxy(ipAddress, port)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
