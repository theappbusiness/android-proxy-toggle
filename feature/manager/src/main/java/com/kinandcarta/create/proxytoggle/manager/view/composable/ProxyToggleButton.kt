package com.kinandcarta.create.proxytoggle.manager.view.composable

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.res.ResourcesCompat
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kinandcarta.create.proxytoggle.core.ui.theme.BluishGrey
import com.kinandcarta.create.proxytoggle.core.ui.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.manager.R

@Composable
fun ProxyToggleButton(
    proxyEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (stateDescription, actionDescription) = Pair(
        stringResource(if (proxyEnabled) R.string.connected else R.string.disconnected),
        stringResource(if (proxyEnabled) R.string.disable_proxy else R.string.enable_proxy)
    )
    val resources = LocalContext.current.resources
    val shadowStart = ResourcesCompat.getFloat(resources, R.dimen.toggle_shadow_start)
    val shadowColor = if (proxyEnabled) MaterialTheme.colorScheme.primary else BluishGrey
    IconButton(
        onClick = onClick,
        modifier = modifier
            .semantics {
                this.stateDescription = stateDescription
                onClick(label = actionDescription, action = { onClick(); true })
            }
            .size(dimensionResource(R.dimen.toggle_shape_plus_shadow_size))
            .background(
                brush = Brush.radialGradient(
                    shadowStart to shadowColor,
                    1f to Color.Transparent
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(dimensionResource(R.dimen.toggle_shape_size))
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            val inset = dimensionResource(R.dimen.toggle_inset)
            val topInset = dimensionResource(R.dimen.toggle_inset_small)
            Image(
                painter = painterResource(R.drawable.ic_power),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = inset, top = topInset, end = inset, bottom = inset)
                    .fillMaxSize()
                    .align(Alignment.Center),
                colorFilter = ColorFilter.tint(
                    if (proxyEnabled) MaterialTheme.colorScheme.primary else BluishGrey
                )
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
@ShowkaseComposable(skip = true)
fun ProxyToggleButtonEnabledPreview() {
    ProxyToggleButtonPreviewContent(proxyEnabled = true)
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
@ShowkaseComposable(skip = true)
fun ProxyToggleButtonDisabledPreview() {
    ProxyToggleButtonPreviewContent()
}

@Composable
private fun ProxyToggleButtonPreviewContent(proxyEnabled: Boolean = false) {
    ProxyToggleTheme {
        Surface {
            ProxyToggleButton(proxyEnabled = proxyEnabled, onClick = {})
        }
    }
}
