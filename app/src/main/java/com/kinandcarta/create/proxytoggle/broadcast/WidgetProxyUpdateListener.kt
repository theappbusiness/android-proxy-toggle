package com.kinandcarta.create.proxytoggle.broadcast

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.kinandcarta.create.proxytoggle.widget.ToggleWidgetProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WidgetProxyUpdateListener @Inject constructor(
    @ApplicationContext private val context: Context
) : com.kinandcarta.create.proxytoggle.broadcast.ProxyUpdateListener {

    override fun onProxyUpdate() {
        val intent = Intent(context, ToggleWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetComponent = ComponentName(context, ToggleWidgetProvider::class.java)
        val ids = appWidgetManager.getAppWidgetIds(widgetComponent)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }
}
