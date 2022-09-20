@file:Suppress("TooManyFunctions")

package com.kinandcarta.create.proxytoggle.manager.view.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kinandcarta.create.proxytoggle.core.model.Proxy
import com.kinandcarta.create.proxytoggle.core.theme.BlueyGrey
import com.kinandcarta.create.proxytoggle.core.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.core.theme.StatusLabelTextStyle
import com.kinandcarta.create.proxytoggle.manager.R
import com.kinandcarta.create.proxytoggle.manager.viewmodel.ProxyManagerViewModel
import com.kinandcarta.create.proxytoggle.manager.viewmodel.TextFieldState

@Composable
fun ProxyManagerScreen(
    viewModel: ProxyManagerViewModel = viewModel(),
    useVerticalLayout: Boolean
) {
    val uiState by viewModel.uiState

    ProxyManagerScreenContent(
        useVerticalLayout = useVerticalLayout,
        darkTheme = uiState.darkTheme,
        onToggleTheme = viewModel::toggleTheme,
        proxyEnabled = uiState.proxyEnabled,
        onToggleProxy = viewModel::toggleProxy,
        addressState = uiState.addressState,
        onAddressChanged = viewModel::onAddressChanged,
        portState = uiState.portState,
        onPortChanged = viewModel::onPortChanged,
        onForceFocusExecuted = viewModel::onForceFocusExecuted
    )
}

@Suppress("LongParameterList")
@Composable
fun ProxyManagerScreenContent(
    useVerticalLayout: Boolean,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    proxyEnabled: Boolean,
    onToggleProxy: () -> Unit,
    addressState: TextFieldState,
    onAddressChanged: (String) -> Unit,
    portState: TextFieldState,
    onPortChanged: (String) -> Unit,
    onForceFocusExecuted: () -> Unit,
    isPreview: Boolean = false
) {
    @OptIn(ExperimentalComposeUiApi::class)
    if (proxyEnabled) {
        LocalSoftwareKeyboardController.current?.hide()
    }

    var showInfoDialog by rememberSaveable { mutableStateOf(false) }

    ProxyToggleTheme(darkTheme = darkTheme, isPreview = isPreview) {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                TopIcons(
                    onToggleTheme = onToggleTheme,
                    onDismissInfo = { showInfoDialog = showInfoDialog.not() },
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                MainLayout(
                    useVerticalLayout = useVerticalLayout,
                    buttonAndLabel = {
                        ButtonAndLabel(
                            proxyEnabled = proxyEnabled,
                            onToggleProxy = onToggleProxy
                        )
                    },
                    addressTextField = {
                        ProxyToggleTextField(
                            state = addressState,
                            onTextChanged = onAddressChanged,
                            enabled = proxyEnabled.not(),
                            onForceFocusExecuted = onForceFocusExecuted
                        )
                    },
                    portTextField = {
                        ProxyToggleTextField(
                            state = portState,
                            onTextChanged = onPortChanged,
                            enabled = proxyEnabled.not(),
                            onForceFocusExecuted = onForceFocusExecuted
                        )
                    }
                )
                if (showInfoDialog) {
                    ProxyToggleAlertDialog(
                        message = stringResource(R.string.dialog_message_information),
                        onCloseDialog = { showInfoDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
fun TopIcons(
    onToggleTheme: () -> Unit,
    onDismissInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        ProxyToggleIcon(
            onClick = onToggleTheme,
            icon = R.drawable.ic_switch_theme,
            contentDescription = R.string.a11y_switch_theme
        )
        ProxyToggleIcon(
            onClick = onDismissInfo,
            icon = R.drawable.ic_info,
            contentDescription = R.string.a11y_information
        )
    }
}

@Composable
fun MainLayout(
    useVerticalLayout: Boolean,
    buttonAndLabel: @Composable () -> Unit,
    addressTextField: @Composable () -> Unit,
    portTextField: @Composable () -> Unit
) {
    if (useVerticalLayout) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.xlarge_margin))
        ) {
            buttonAndLabel()
            Spacer(Modifier.height(dimensionResource(R.dimen.large_margin)))
            addressTextField()
            Spacer(Modifier.height(dimensionResource(R.dimen.default_margin)))
            portTextField()
        }
    } else {
        Row(
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.xlarge_margin))
        ) {
            Column {
                Spacer(Modifier.height(dimensionResource(R.dimen.default_margin)))
                addressTextField()
                Spacer(Modifier.height(dimensionResource(R.dimen.default_margin)))
                portTextField()
            }
            Spacer(Modifier.width(dimensionResource(R.dimen.large_margin)))
            buttonAndLabel()
        }
    }
}

@Composable
fun ButtonAndLabel(
    proxyEnabled: Boolean,
    onToggleProxy: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
    ) {
        ProxyToggleButton(
            proxyEnabled = proxyEnabled,
            onClick = onToggleProxy
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.default_margin)))
        Text(
            text = stringResource(
                if (proxyEnabled) R.string.connected else R.string.disconnected
            ).uppercase(),
            color = if (proxyEnabled) MaterialTheme.colors.primary else BlueyGrey,
            modifier = Modifier.clearAndSetSemantics {},
            style = StatusLabelTextStyle
        )
    }
}

@Preview(name = "Connected (Light)", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenConnectedPreview() {
    ProxyManagerScreenForPreview(proxyEnabled = true)
}

@Preview(name = "Connected (Dark)", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenConnectedPreviewDark() {
    ProxyManagerScreenForPreview(darkTheme = true, proxyEnabled = true)
}

@Preview(name = "Unconnected (Light)", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenUnconnectedPreview() {
    ProxyManagerScreenForPreview()
}

@Preview(name = "Unconnected (Dark)", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenUnconnectedPreviewDark() {
    ProxyManagerScreenForPreview(darkTheme = true)
}

private const val PREVIEW_LANDSCAPE_WIDTH = 800
private const val PREVIEW_LANDSCAPE_HEIGHT = 500

@Preview(
    name = "Connected (Light Landscape)", group = "ProxyManagerScreen Landscape",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = PREVIEW_LANDSCAPE_WIDTH,
    heightDp = PREVIEW_LANDSCAPE_HEIGHT
)
@Composable
fun ProxyManagerScreenConnectedPreviewLandscape() {
    ProxyManagerScreenForPreview(useVerticalLayout = false, proxyEnabled = true)
}

@Preview(
    name = "Connected (Dark Landscape)", group = "ProxyManagerScreen Landscape",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = PREVIEW_LANDSCAPE_WIDTH,
    heightDp = PREVIEW_LANDSCAPE_HEIGHT
)
@Composable
fun ProxyManagerScreenConnectedPreviewDarkLandscape() {
    ProxyManagerScreenForPreview(
        useVerticalLayout = false,
        darkTheme = true,
        proxyEnabled = true
    )
}

@Preview(
    name = "Unconnected (Light Landscape)", group = "ProxyManagerScreen Landscape",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = PREVIEW_LANDSCAPE_WIDTH,
    heightDp = PREVIEW_LANDSCAPE_HEIGHT
)
@Composable
fun ProxyManagerScreenUnconnectedPreviewLandscape() {
    ProxyManagerScreenForPreview(useVerticalLayout = false)
}

@Preview(
    name = "Unconnected (Dark Landscape)", group = "ProxyManagerScreen Landscape",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = PREVIEW_LANDSCAPE_WIDTH,
    heightDp = PREVIEW_LANDSCAPE_HEIGHT
)
@Composable
fun ProxyManagerScreenUnconnectedPreviewDarkLandscape() {
    ProxyManagerScreenForPreview(useVerticalLayout = false, darkTheme = true)
}

@Composable
private fun ProxyManagerScreenForPreview(
    useVerticalLayout: Boolean = true,
    darkTheme: Boolean = false,
    proxyEnabled: Boolean = false
) {
    val someProxy = Proxy("192.168.1.1", "8888")
    ProxyManagerScreenContent(
        useVerticalLayout = useVerticalLayout,
        darkTheme = darkTheme,
        onToggleTheme = {},
        proxyEnabled = proxyEnabled,
        onToggleProxy = {},
        addressState = TextFieldState(
            label = "IP Address",
            text = if (proxyEnabled) someProxy.address else "",
            keyboardOptions = KeyboardOptions.Default
        ),
        onAddressChanged = {},
        portState = TextFieldState(
            label = "Port",
            text = if (proxyEnabled) someProxy.port else "",
            keyboardOptions = KeyboardOptions.Default
        ),
        onPortChanged = {},
        onForceFocusExecuted = {},
        isPreview = true
    )
}
