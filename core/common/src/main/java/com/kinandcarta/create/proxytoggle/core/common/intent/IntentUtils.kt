package com.kinandcarta.create.proxytoggle.core.common.intent

import android.content.Context
import android.content.Intent

fun getAppLaunchIntent(context: Context): Intent? {
    return context.packageManager.getLaunchIntentForPackage(context.packageName)
        ?.apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
}
