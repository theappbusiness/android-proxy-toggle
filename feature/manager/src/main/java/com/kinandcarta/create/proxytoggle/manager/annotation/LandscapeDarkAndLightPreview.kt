package com.kinandcarta.create.proxytoggle.manager.annotation

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

// SOS: showkase doesn't work if I move this to :core!
@Preview(
    group = "Landscape Light",
    device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480"
)
@Preview(
    group = "Landscape Dark",
    device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class LandscapeDarkAndLightPreview
