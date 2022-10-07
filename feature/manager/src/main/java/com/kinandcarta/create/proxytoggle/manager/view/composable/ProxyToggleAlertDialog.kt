package com.kinandcarta.create.proxytoggle.manager.view.composable

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kinandcarta.create.proxytoggle.core.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.manager.R

@Composable
fun ProxyToggleAlertDialog(
    title: String? = null,
    message: String,
    onCloseDialog: (() -> Unit)? = null
) {
    AlertDialog(
        title = title?.let { { Text(text = it) } },
        text = { Text(text = message, color = MaterialTheme.colors.onSurface) },
        confirmButton = {
            onCloseDialog?.let {
                TextButton(onClick = it) {
                    Text(text = stringResource(R.string.dialog_action_close).uppercase())
                }
            }
        },
        onDismissRequest = onCloseDialog ?: {},
    )
}

@Preview(name = "Info Dialog (Light)", group = "ProxyToggleAlertDialog")
@ShowkaseComposable(skip = true)
@Composable
fun ProxyToggleAlertDialogPreview() {
    ProxyToggleAlertDialogPreviewContent()
}

@Preview(name = "Info Dialog (Dark)", group = "ProxyToggleAlertDialog")
@ShowkaseComposable(skip = true)
@Composable
fun ProxyToggleAlertDialogPreviewDark() {
    ProxyToggleAlertDialogPreviewContent(darkTheme = true)
}

@Composable
private fun ProxyToggleAlertDialogPreviewContent(darkTheme: Boolean = false) {
    ProxyToggleTheme(darkTheme = darkTheme) {
        ProxyToggleAlertDialog(
            message = stringResource(R.string.dialog_message_information),
            onCloseDialog = {}
        )
    }
}
