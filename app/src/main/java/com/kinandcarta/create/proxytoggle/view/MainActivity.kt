package com.kinandcarta.create.proxytoggle.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kinandcarta.create.proxytoggle.R
import com.kinandcarta.create.proxytoggle.view.manager.ProxyManagerFragment
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
