package com.kinandcarta.create.proxytoggle.manager.annotation

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

// SOS: showkase doesn't work if I move this to :core!
@Preview(
    group = "Portrait Light",
    device = Devices.PIXEL
)
@Preview(
    group = "Portrait Dark",
    device = Devices.PIXEL,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class DarkAndLightPreview
