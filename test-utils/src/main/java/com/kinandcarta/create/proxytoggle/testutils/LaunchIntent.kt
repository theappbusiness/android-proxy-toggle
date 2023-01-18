package com.kinandcarta.create.proxytoggle.testutils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.mockk.every
import org.robolectric.Shadows

private const val PACKAGE_NAME = "com.kinandcarta.create.proxytoggle"
private const val MAIN_ACTIVITY_NAME = "MainActivity"

fun addMainActivityToRobolectric(context: Context) {
    every { context.packageName } returns PACKAGE_NAME
    val packageManager = Shadows.shadowOf(context.packageManager)
    packageManager.apply {
        addActivityIfNotPresent(ComponentName(context, MAIN_ACTIVITY_NAME))
        addIntentFilterForActivity(
            ComponentName(context, MAIN_ACTIVITY_NAME),
            IntentFilter(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
        )
    }
}

fun expectedLaunchIntent(context: Context): Intent {
    return Intent().apply {
        action = Intent.ACTION_MAIN
        addCategory(Intent.CATEGORY_LAUNCHER)
        setPackage(PACKAGE_NAME)
        component = ComponentName(context, MAIN_ACTIVITY_NAME)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
}
