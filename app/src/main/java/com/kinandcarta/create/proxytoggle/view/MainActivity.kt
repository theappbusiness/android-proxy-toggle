package com.kinandcarta.create.proxytoggle.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.kinandcarta.create.proxytoggle.manager.view.composable.BlockAppScreen
import com.kinandcarta.create.proxytoggle.manager.view.composable.ProxyManagerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
            val heightSizeClass = calculateWindowSizeClass(this).heightSizeClass
            val useVerticalLayout = heightSizeClass != WindowHeightSizeClass.Compact

            val showBlockDialog = ContextCompat.checkSelfPermission(
                LocalContext.current,
                Manifest.permission.WRITE_SECURE_SETTINGS
            ) == PackageManager.PERMISSION_DENIED

            if (showBlockDialog) {
                BlockAppScreen()
            } else {
                ProxyManagerScreen(useVerticalLayout = useVerticalLayout)
            }
        }
    }
}
