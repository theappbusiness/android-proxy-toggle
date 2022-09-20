package com.kinandcarta.create.proxytoggle.manager.view.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import com.kinandcarta.create.proxytoggle.core.theme.BlueyGrey
import com.kinandcarta.create.proxytoggle.core.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.manager.R

@Composable
fun ProxyToggleButton(
    proxyEnabled: Boolean,
    onClick: () -> Unit
) {
    val (stateDescription, actionDescription) = Pair(
        stringResource(if (proxyEnabled) R.string.connected else R.string.disconnected),
        stringResource(if (proxyEnabled) R.string.a11y_disable_proxy else R.string.a11y_enable_proxy)
    )
    val resources = LocalContext.current.resources
    val shadowStart = ResourcesCompat.getFloat(resources, R.dimen.toggle_shadow_start)
    val shadowColor = if (proxyEnabled) MaterialTheme.colors.primary else BlueyGrey
    IconButton(
        onClick = onClick,
        modifier = Modifier.semantics {
            this.stateDescription = stateDescription
            onClick(label = actionDescription, action = { onClick(); true })
        }
    ) {
        Box(
            modifier = Modifier
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
                    .background(MaterialTheme.colors.surface)
                    .align(Alignment.Center)
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
                        if (proxyEnabled) MaterialTheme.colors.primary else BlueyGrey
                    )
                )
            }
        }
    }
}

@Preview(name = "Enabled (Light)", group = "ProxyToggleButton")
@Composable
fun ProxyToggleButtonEnabledPreview() {
    ProxyToggleButtonPreviewContent(proxyEnabled = true)
}

@Preview(name = "Enabled (Dark)", group = "ProxyToggleButton")
@Composable
fun ProxyToggleButtonEnabledPreviewDark() {
    ProxyToggleButtonPreviewContent(darkTheme = true, proxyEnabled = true)
}

@Preview(name = "Disabled (Light)", group = "ProxyToggleButton")
@Composable
fun ProxyToggleButtonDisabledPreview() {
    ProxyToggleButtonPreviewContent()
}

@Preview(name = "Disabled (Dark)", group = "ProxyToggleButton")
@Composable
fun ProxyToggleButtonDisabledPreviewDark() {
    ProxyToggleButtonPreviewContent(darkTheme = true)
}

@Composable
private fun ProxyToggleButtonPreviewContent(
    darkTheme: Boolean = false,
    proxyEnabled: Boolean = false
) {
    ProxyToggleTheme(darkTheme = darkTheme, isPreview = true) {
        Surface {
            ProxyToggleButton(proxyEnabled = proxyEnabled, onClick = {})
        }
    }
}
