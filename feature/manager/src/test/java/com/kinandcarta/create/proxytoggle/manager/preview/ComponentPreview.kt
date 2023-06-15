package com.kinandcarta.create.proxytoggle.manager.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent

class ComponentPreview(
    private val skbComponent: ShowkaseBrowserComponent
) {
    val content: @Composable () -> Unit = skbComponent.component

    val group = skbComponent.group

    override fun toString(): String {
        return skbComponent.group + ":" + skbComponent.componentName
    }
}

@Composable
fun ComponentPreview.contentWithUiMode() {
    val configuration = Configuration(LocalConfiguration.current)
    if (this.group.contains("Dark")) {
        configuration.uiMode = Configuration.UI_MODE_NIGHT_YES
    }
    CompositionLocalProvider(LocalConfiguration provides configuration) {
        this.content()
    }
}
