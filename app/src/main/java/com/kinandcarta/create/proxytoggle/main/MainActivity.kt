package com.kinandcarta.create.proxytoggle.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.kinandcarta.create.proxytoggle.core.ui.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.manager.view.screen.BlockAppScreen
import com.kinandcarta.create.proxytoggle.manager.view.screen.ProxyManagerScreen
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val proxyViewModel: ProxyManagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val useDarkTheme by viewModel.useDarkTheme.collectAsState()

            @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
            val heightSizeClass = calculateWindowSizeClass(this).heightSizeClass
            val useVerticalLayout = heightSizeClass != WindowHeightSizeClass.Compact

            MainScreen(useDarkTheme = useDarkTheme, useVerticalLayout = useVerticalLayout)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.getStringExtra(ProxyManagerViewModel.PROXY_KEY)?.takeIf { it.isNotBlank() }?.let {
            Log.i("Intent", "On new intent $it")
            proxyViewModel.onSetProxy(it)
        }
    }
}

@Composable
private fun MainScreen(useDarkTheme: Boolean, useVerticalLayout: Boolean) {
    val showBlockDialog = ContextCompat.checkSelfPermission(
        LocalContext.current,
        Manifest.permission.WRITE_SECURE_SETTINGS
    ) == PackageManager.PERMISSION_DENIED

    ProxyToggleTheme(darkTheme = useDarkTheme) {
        if (showBlockDialog) {
            BlockAppScreen()
        } else {
            ProxyManagerScreen(useVerticalLayout = useVerticalLayout)
        }
    }
}
