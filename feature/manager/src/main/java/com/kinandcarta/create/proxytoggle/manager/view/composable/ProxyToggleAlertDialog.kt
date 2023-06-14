package com.kinandcarta.create.proxytoggle.manager.view.composable

import android.content.res.Configuration
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kinandcarta.create.proxytoggle.core.ui.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.manager.R

@Composable
fun ProxyToggleAlertDialog(
    title: String? = null,
    message: String,
    onCloseDialog: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onCloseDialog ?: {},
        confirmButton = {
            onCloseDialog?.let {
                TextButton(onClick = it) {
                    Text(text = stringResource(R.string.dialog_action_close).uppercase())
                }
            }
        },
        title = title?.let { { Text(text = it) } },
        text = { Text(text = message, color = MaterialTheme.colorScheme.onSurface) },
        shape = RoundedCornerShape(8.dp)
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@ShowkaseComposable(skip = true)
@Composable
fun ProxyToggleAlertDialogPreview() {
    ProxyToggleAlertDialogPreviewContent()
}

@Composable
private fun ProxyToggleAlertDialogPreviewContent() {
    ProxyToggleTheme {
        ProxyToggleAlertDialog(
            message = stringResource(R.string.dialog_message_information),
            onCloseDialog = {}
        )
    }
}
