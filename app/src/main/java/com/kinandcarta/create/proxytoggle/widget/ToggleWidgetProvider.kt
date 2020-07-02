package com.kinandcarta.create.proxytoggle.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.kinandcarta.create.proxytoggle.R
import com.kinandcarta.create.proxytoggle.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.model.Proxy
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.managers.BroadcastReceiverComponentManager
import dagger.hilt.internal.UnsafeCasts
import javax.inject.Inject

@AndroidEntryPoint
class ToggleWidgetProvider : AppWidgetProvider() {

    // TODO Handle the scenario when the user adds the widget before the proxy

    companion object {
        private const val ACTION_PROXY_ENABLE = "Enable Proxy"
        private const val ACTION_PROXY_DISABLE = "Disable Proxy"
    }

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
                buildEnabledView(remoteView, proxy, context)
            } else {
                buildDisabledView(remoteView, context)
            }

            appWidgetManager.updateAppWidget(appWidgetId, remoteView)
        }
    }

    private fun buildDisabledView(remoteView: RemoteViews, context: Context) {
        remoteView.apply {
            setTextViewText(R.id.button, "Enable")
            setViewVisibility(R.id.proxy, View.GONE)
            setOnClickPendingIntent(
                R.id.button,
                ACTION_PROXY_ENABLE.asPendingIntent(context)
            )
        }
    }

    private fun buildEnabledView(remoteView: RemoteViews, proxy: Proxy, context: Context) {
        remoteView.apply {
            setTextViewText(R.id.button, "Disable")
            setViewVisibility(R.id.proxy, View.VISIBLE)
            setTextViewText(R.id.proxy, proxy.toString())
            setOnClickPendingIntent(
                R.id.button,
                ACTION_PROXY_DISABLE.asPendingIntent(context)
            )
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Hilt has some issues injecting onReceive() BroadcastReceivers:
        // More info: https://github.com/google/dagger/issues/1918
        // TODO Remove me when Hilt is updated
        (BroadcastReceiverComponentManager.generatedComponent(context) as
                ToggleWidgetProvider_GeneratedInjector).injectToggleWidgetProvider(
            UnsafeCasts.unsafeCast<ToggleWidgetProvider>(this)
        )

        when (intent.action) {
            ACTION_PROXY_ENABLE -> enableProxy()
            ACTION_PROXY_DISABLE -> disableProxy()
            else -> super.onReceive(context, intent)
        }
    }

    private fun enableProxy() {
        // TODO Take this from SharedPrefs once the user is able to input
        deviceSettingsManager.enableProxy(Proxy("192.168.1.215", "8888"))
    }

    private fun disableProxy() {
        deviceSettingsManager.disableProxy()
    }

    private fun String.asPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, ToggleWidgetProvider::class.java).apply {
            action = this@asPendingIntent
        }
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
