package com.kinandcarta.create.proxytoggle.manager.view.extension

import android.view.KeyEvent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent

@OptIn(ExperimentalComposeUiApi::class)
object ModifierExt {
    fun Modifier.shiftFocusToNextOnTabModifier(focusManager: FocusManager): Modifier {
        return this.onPreviewKeyEvent {
            if (it.key == Key.Tab && it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                focusManager.moveFocus(FocusDirection.Next)
                true
            } else {
                false
            }
        }
    }
}
