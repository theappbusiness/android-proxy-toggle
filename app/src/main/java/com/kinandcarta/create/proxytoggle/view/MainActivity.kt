package com.kinandcarta.create.proxytoggle.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kinandcarta.create.proxytoggle.R
import com.kinandcarta.create.proxytoggle.feature.manager.view.ProxyManagerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.main_activity) {

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS)) {
            PackageManager.PERMISSION_DENIED -> showPermissionRequiredDialog()
            PackageManager.PERMISSION_GRANTED -> showApplicationContent(savedInstanceState)
        }
    }

    private fun showPermissionRequiredDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title_special_permissions))
            .setMessage(getString(R.string.dialog_message_special_permissions))
            .setCancelable(false)
            .create()
            .show()
    }

    private fun showApplicationContent(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProxyManagerFragment.newInstance())
                .commitNow()
        }
    }
}
