package com.kinandcarta.create.proxytoggle.manager.view.composable

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kinandcarta.create.proxytoggle.core.ui.theme.ProxyToggleTheme
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
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
@ShowkaseComposable(skip = true)
fun ProxyToggleIconPreview() {
    ProxyToggleIconPreviewContent()
}

@Composable
private fun ProxyToggleIconPreviewContent() {
    ProxyToggleTheme {
        Surface {
            ProxyToggleIcon(
                onClick = {},
                icon = R.drawable.ic_info,
                contentDescription = R.string.information
            )
        }
    }
}
