package com.kinandcarta.create.proxytoggle.manager.view.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kinandcarta.create.proxytoggle.core.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.manager.R

@Composable
fun ProxyToggleIcon(
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    @StringRes contentDescription: Int,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(contentDescription),
            tint = MaterialTheme.colors.primary
        )
    }
}

@Preview(name = "Info Icon (Light)", group = "ProxyToggleIcon")
@Composable
@ShowkaseComposable(skip = true)
fun ProxyToggleIconPreview() {
    ProxyToggleIconPreviewContent()
}

@Preview(name = "Info Icon (Dark)", group = "ProxyToggleIcon")
@Composable
@ShowkaseComposable(skip = true)
fun ProxyToggleIconPreviewDark() {
    ProxyToggleIconPreviewContent(darkTheme = true)
}

@Composable
private fun ProxyToggleIconPreviewContent(darkTheme: Boolean = false) {
    ProxyToggleTheme(darkTheme = darkTheme) {
        Surface {
            ProxyToggleIcon(
                onClick = {},
                icon = R.drawable.ic_info,
                contentDescription = R.string.a11y_information
            )
        }
    }
}
