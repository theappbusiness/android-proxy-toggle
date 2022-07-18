package com.kinandcarta.create.proxytoggle.core.intent

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

fun getLaunchIntent(context: Context): Intent? {
    return context.packageManager.getLaunchIntentForPackage(context.packageName)
        ?.apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
}

fun getPendingIntentFlags(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
}
