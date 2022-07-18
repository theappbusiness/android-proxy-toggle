package com.kinandcarta.create.proxytoggle.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.kinandcarta.create.proxytoggle.core.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.core.intent.getLaunchIntent
import com.kinandcarta.create.proxytoggle.core.intent.getPendingIntentFlags
import com.kinandcarta.create.proxytoggle.core.model.Proxy
import com.kinandcarta.create.proxytoggle.core.settings.AppSettings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ToggleWidgetProvider : AppWidgetProvider() {

    companion object {
        private const val ACTION_PROXY_ENABLE = "Enable Proxy"
        private const val ACTION_PROXY_DISABLE = "Disable Proxy"
    }

    @Inject
    lateinit var deviceSettingsManager: DeviceSettingsManager

    @Inject
    lateinit var appSettings: AppSettings

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

            getLaunchIntent(context)?.let {
                remoteView.setOnClickPendingIntent(
                    R.id.settings,
                    PendingIntent.getActivity(context, 0, it, getPendingIntentFlags())
                )
            }

            appWidgetManager.updateAppWidget(appWidgetId, remoteView)
        }
    }

    private fun buildDisabledView(remoteView: RemoteViews, context: Context) {
        remoteView.apply {
            val lastUsedProxy = appSettings.lastUsedProxy
            if (lastUsedProxy.isEnabled) {
                setTextViewText(R.id.address, lastUsedProxy.address)
                setTextViewText(R.id.port, lastUsedProxy.port)
            } else {
                setTextViewText(R.id.address, context.getString(R.string.widget_not_set))
                setTextViewText(R.id.port, context.getString(R.string.widget_not_set))
            }
            setTextViewText(R.id.status, context.getString(R.string.proxy_status_disabled))
            setTextColor(
                R.id.status,
                ContextCompat.getColor(context, R.color.widget_label_disabled)
            )
            setImageViewResource(R.id.toggle, R.drawable.widget_toggle_disabled)
            setOnClickPendingIntent(
                R.id.toggle,
                ACTION_PROXY_ENABLE.asPendingIntent(context)
            )
        }
    }

    private fun buildEnabledView(remoteView: RemoteViews, proxy: Proxy, context: Context) {
        remoteView.apply {
            setTextViewText(R.id.address, proxy.address)
            setTextViewText(R.id.port, proxy.port)
            setTextViewText(R.id.status, context.getString(R.string.proxy_status_enabled))
            setTextColor(R.id.status, ContextCompat.getColor(context, R.color.widget_label_enabled))
            setImageViewResource(R.id.toggle, R.drawable.widget_toggle_enabled)
            setOnClickPendingIntent(
                R.id.toggle,
                ACTION_PROXY_DISABLE.asPendingIntent(context)
            )
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_PROXY_ENABLE -> enableProxy(context)
            ACTION_PROXY_DISABLE -> disableProxy()
        }
    }

    private fun enableProxy(context: Context) {
        val lastUsedProxy = appSettings.lastUsedProxy
        if (lastUsedProxy.isEnabled) {
            deviceSettingsManager.enableProxy(lastUsedProxy)
        } else {
            // There is no last used Proxy, prompt the user to create one
            getLaunchIntent(context)?.let { context.startActivity(it) }
        }
    }

    private fun disableProxy() {
        deviceSettingsManager.disableProxy()
    }

    private fun String.asPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, ToggleWidgetProvider::class.java).apply {
            action = this@asPendingIntent
        }
        return PendingIntent.getBroadcast(context, 0, intent, getPendingIntentFlags())
    }
}
