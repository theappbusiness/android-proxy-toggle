package com.kinandcarta.create.proxytoggle.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.kinandcarta.create.proxytoggle.lib.core.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.lib.core.model.Proxy
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ToggleWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var deviceSettingsManager: DeviceSettingsManager

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            // Instantiate the RemoteViews object for the app widget layout.
            val remoteView = RemoteViews(context.packageName, R.layout.widget_toggle)

            val proxy = deviceSettingsManager.proxySetting.value ?: Proxy.Disabled

            if (proxy.isEnabled) {
                remoteView.setTextViewText(R.id.button, "Disable")
                remoteView.setViewVisibility(R.id.proxy, View.VISIBLE)
                remoteView.setTextViewText(R.id.proxy, proxy.toString())
            } else {
                remoteView.setTextViewText(R.id.button, "Enable")
                remoteView.setViewVisibility(R.id.proxy, View.GONE)
            }

            appWidgetManager.updateAppWidget(appWidgetId, remoteView)
        }
    }
}
