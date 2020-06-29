package com.kinandcarta.create.proxytoggle.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kinandcarta.create.proxytoggle.manager.ui.ProxyManagerFragment
import com.kinandcarta.create.proxytoggle.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.main_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProxyManagerFragment.newInstance())
                .commitNow()
        }
    }
}
