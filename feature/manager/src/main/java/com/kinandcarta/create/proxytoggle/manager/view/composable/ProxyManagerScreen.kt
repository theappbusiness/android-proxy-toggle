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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.kinandcarta.create.proxytoggle.core.theme.BlueyGrey
import com.kinandcarta.create.proxytoggle.core.theme.ProxyToggleTheme
import com.kinandcarta.create.proxytoggle.core.theme.StatusLabelTextStyle
import com.kinandcarta.create.proxytoggle.manager.R
import com.kinandcarta.create.proxytoggle.manager.mapper.AddressAndPort
import com.kinandcarta.create.proxytoggle.manager.mapper.InputErrors

@Suppress("LongMethod", "LongParameterList")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProxyManagerScreen(
    useVerticalLayout: Boolean,
    activeProxyData: AddressAndPort?,
    lastProxyUsedData: AddressAndPort?,
    inputErrors: InputErrors,
    onToggleTheme: () -> Unit,
    onToggleProxy: (String, String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var ipAddressText by remember { mutableStateOf("") }
    var portText by remember { mutableStateOf("") }
    if (activeProxyData != null) {
        ipAddressText = activeProxyData.address
        portText = activeProxyData.port
        keyboardController?.hide()
    }
    LaunchedEffect(true) {
        if (ipAddressText.isEmpty() && portText.isEmpty() && lastProxyUsedData != null) {
            ipAddressText = lastProxyUsedData.address
            portText = lastProxyUsedData.port
        }
    }
    var showInfoDialog by rememberSaveable { mutableStateOf(false) }
    var lastClickTime by rememberSaveable { mutableStateOf(System.nanoTime()) }

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
                        proxyEnabled = activeProxyData != null,
                        onClick = {
                            lastClickTime = System.nanoTime()
                            onToggleProxy(ipAddressText, portText)
                        }
                    )
                },
                ipAddressTextField = {
                    ProxyToggleTextField(
                        texts = TextFieldTexts(
                            text = ipAddressText,
                            label = stringResource(id = R.string.hint_ip_address),
                            errorText = inputErrors.addressError
                        ),
                        onTextChanged = { ipAddressText = it },
                        enabled = activeProxyData == null,
                        keyboardOptions = getKeyboardOptions(KeyboardType.Uri, ImeAction.Next),
                        lastClickTime = lastClickTime
                    )
                },
                portTextField = {
                    ProxyToggleTextField(
                        texts = TextFieldTexts(
                            text = portText,
                            label = stringResource(id = R.string.hint_port),
                            errorText = inputErrors.portError
                        ),
                        onTextChanged = { portText = it },
                        enabled = activeProxyData == null,
                        keyboardOptions = getKeyboardOptions(KeyboardType.Number, ImeAction.Done),
                        lastClickTime = lastClickTime
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
    ipAddressTextField: @Composable () -> Unit,
    portTextField: @Composable () -> Unit
) {
    if (useVerticalLayout) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.xlarge_margin))
        ) {
            buttonAndLabel()
            Spacer(Modifier.height(dimensionResource(R.dimen.large_margin)))
            ipAddressTextField()
            Spacer(Modifier.height(dimensionResource(R.dimen.default_margin)))
            portTextField()
        }
    } else {
        Row(
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.xlarge_margin))
        ) {
            Column {
                Spacer(Modifier.height(dimensionResource(R.dimen.default_margin)))
                ipAddressTextField()
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
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ProxyToggleButton(
            proxyEnabled = proxyEnabled,
            onClick = onClick
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.default_margin)))
        StatusLabel(connected = proxyEnabled)
    }
}

@Composable
fun StatusLabel(connected: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(
            if (connected) R.string.proxy_status_enabled else R.string.proxy_status_disabled
        ).uppercase(),
        modifier = modifier,
        color = if (connected) MaterialTheme.colors.primary else BlueyGrey,
        style = StatusLabelTextStyle
    )
}

@Preview(name = "Connected Light", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenConnectedPreview() {
    val someProxy = AddressAndPort("192.168.1.1", "8888")
    ProxyManagerScreenForPreview(activeProxyData = someProxy)
}

@Preview(name = "Connected Dark", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenConnectedPreviewDark() {
    val someProxy = AddressAndPort("192.168.1.1", "8888")
    ProxyManagerScreenForPreview(darkTheme = true, activeProxyData = someProxy)
}

@Preview(name = "Unconnected Light", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenUnconnectedPreview() {
    ProxyManagerScreenForPreview()
}

@Preview(name = "Unconnected Dark", group = "ProxyManagerScreen")
@Composable
fun ProxyManagerScreenUnconnectedPreviewDark() {
    ProxyManagerScreenForPreview(darkTheme = true)
}

private const val PREVIEW_LANDSCAPE_WIDTH = 800
private const val PREVIEW_LANDSCAPE_HEIGHT = 500

@Preview(
    name = "Connected Light Landscape", group = "ProxyManagerScreen Landscape",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = PREVIEW_LANDSCAPE_WIDTH,
    heightDp = PREVIEW_LANDSCAPE_HEIGHT
)
@Composable
fun ProxyManagerScreenConnectedPreviewLandscape() {
    val someProxy = AddressAndPort("192.168.1.1", "8888")
    ProxyManagerScreenForPreview(useVerticalLayout = false, activeProxyData = someProxy)
}

@Preview(
    name = "Connected Dark Landscape", group = "ProxyManagerScreen Landscape",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = PREVIEW_LANDSCAPE_WIDTH,
    heightDp = PREVIEW_LANDSCAPE_HEIGHT
)
@Composable
fun ProxyManagerScreenConnectedPreviewDarkLandscape() {
    val someProxy = AddressAndPort("192.168.1.1", "8888")
    ProxyManagerScreenForPreview(
        useVerticalLayout = false,
        darkTheme = true,
        activeProxyData = someProxy
    )
}

@Preview(
    name = "Unconnected Light Landscape", group = "ProxyManagerScreen Landscape",
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = PREVIEW_LANDSCAPE_WIDTH,
    heightDp = PREVIEW_LANDSCAPE_HEIGHT
)
@Composable
fun ProxyManagerScreenUnconnectedPreviewLandscape() {
    ProxyManagerScreenForPreview(useVerticalLayout = false)
}

@Preview(
    name = "Unconnected Dark Landscape", group = "ProxyManagerScreen Landscape",
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
    activeProxyData: AddressAndPort? = null,
) {
    ProxyToggleTheme(darkTheme = darkTheme, isPreview = true) {
        ProxyManagerScreen(
            useVerticalLayout = useVerticalLayout,
            activeProxyData = activeProxyData,
            lastProxyUsedData = null,
            inputErrors = InputErrors(null, null),
            onToggleTheme = { },
            onToggleProxy = { _, _ -> }
        )
    }
}
