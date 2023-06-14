package com.kinandcarta.create.proxytoggle.manager.view.screen

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kinandcarta.create.proxytoggle.core.ui.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.manager.R
import com.kinandcarta.create.proxytoggle.manager.view.composable.ProxyToggleAlertDialog

@Composable
fun BlockAppScreen() {
    BlockAppScreenContent()
}

@Composable
private fun BlockAppScreenContent() {
    Surface {
        ProxyToggleAlertDialog(
            title = stringResource(R.string.dialog_title_special_permissions),
            message = stringResource(R.string.dialog_message_special_permissions)
        )
    }
}

// FIX: remove skip from dialogs when/if paparazzi is able to capture them
@Preview(name = "Dialog (Light)", group = "BlockAppScreen")
@Preview(name = "Dialog (Dark)", group = "BlockAppScreen", uiMode = Configuration.UI_MODE_NIGHT_YES)
@ShowkaseComposable(skip = true)
@Composable
internal fun BlockAppScreenPreview() {
    ProxyToggleTheme {
        BlockAppScreenContent()
    }
}
