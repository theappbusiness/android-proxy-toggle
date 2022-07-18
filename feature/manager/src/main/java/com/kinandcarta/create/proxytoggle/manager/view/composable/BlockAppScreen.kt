package com.kinandcarta.create.proxytoggle.manager.view.composable

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kinandcarta.create.proxytoggle.core.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.manager.R

@Composable
fun BlockAppScreen() {
    Surface {
        ProxyToggleAlertDialog(
            title = stringResource(R.string.dialog_title_special_permissions),
            message = stringResource(R.string.dialog_message_special_permissions)
        )
    }
}

// FIX: remove skip from dialogs when/if paparazzi is able to capture them
@Preview(name = "Block App Dialog (Light)", group = "BlockAppScreen")
@ShowkaseComposable(skip = true)
@Composable
fun BlockAppScreenPreview() {
    ProxyToggleTheme(isPreview = true) {
        BlockAppScreen()
    }
}

@Preview(name = "Block App Dialog (Dark)", group = "BlockAppScreen")
@ShowkaseComposable(skip = true)
@Composable
fun BlockAppScreenPreviewDark() {
    ProxyToggleTheme(darkTheme = true, isPreview = true) {
        BlockAppScreen()
    }
}
