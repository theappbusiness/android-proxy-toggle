package com.kinandcarta.create.proxytoggle.manager.view.composable

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent

class ComponentPreview(
    private val showkaseBrowserComponent: ShowkaseBrowserComponent
) {
    val content: @Composable () -> Unit = showkaseBrowserComponent.component

    val group = showkaseBrowserComponent.group

    override fun toString(): String {
        return showkaseBrowserComponent.group + ":" + showkaseBrowserComponent.componentName
    }
}
