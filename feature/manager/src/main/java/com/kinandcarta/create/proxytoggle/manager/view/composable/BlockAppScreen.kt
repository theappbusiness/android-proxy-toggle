package com.kinandcarta.create.proxytoggle.manager.view.composable

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kinandcarta.create.proxytoggle.core.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.manager.R
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerViewModel

@Composable
fun BlockAppScreen(
    viewModel: ProxyManagerViewModel = viewModel()
) {
    val uiState by viewModel.uiState
    BlockAppScreenContent(darkTheme = uiState.darkTheme)
}

@Composable
fun BlockAppScreenContent(darkTheme: Boolean, isPreview: Boolean = false) {
    ProxyToggleTheme(darkTheme = darkTheme, isPreview = isPreview) {
        Surface {
            ProxyToggleAlertDialog(
                title = stringResource(R.string.dialog_title_special_permissions),
                message = stringResource(R.string.dialog_message_special_permissions)
            )
        }
    }
}

// FIX: remove skip from dialogs when/if paparazzi is able to capture them
@Preview(name = "Dialog (Light)", group = "BlockAppScreen")
@ShowkaseComposable(skip = true)
@Composable
fun BlockAppScreenPreview() {
    BlockAppScreenContent(darkTheme = false, isPreview = true)
}

@Preview(name = "Dialog (Dark)", group = "BlockAppScreen")
@ShowkaseComposable(skip = true)
@Composable
fun BlockAppScreenPreviewDark() {
    BlockAppScreenContent(darkTheme = true, isPreview = true)
}
