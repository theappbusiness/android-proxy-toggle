package com.kinandcarta.create.proxytoggle.manager.view.screen

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL
import app.cash.paparazzi.Paparazzi
import com.airbnb.android.showkase.models.Showkase
import com.android.resources.Density
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.kinandcarta.create.proxytoggle.manager.getMetadata
import com.kinandcarta.create.proxytoggle.manager.preview.ComponentPreview
import com.kinandcarta.create.proxytoggle.manager.preview.contentWithUiMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class ProxyManagerScreenSnapshots {

    object PreviewProvider : TestParameter.TestParameterValuesProvider {
        override fun provideValues(): List<ComponentPreview> {
            return Showkase.getMetadata().componentList.map(::ComponentPreview)
        }
    }

    private val devicePortrait = PIXEL.copy(softButtons = false)

    private val deviceLandscape = DeviceConfig(
        screenWidth = 1024,
        screenHeight = 768,
        density = Density.HIGH,
        softButtons = false
    )

    @get:Rule
    val paparazzi = Paparazzi(
        maxPercentDifference = 0.0,
        deviceConfig = devicePortrait
    )

    @Test
    fun snapshots(@TestParameter(valuesProvider = PreviewProvider::class) preview: ComponentPreview) {
        if (preview.group.contains("landscape", ignoreCase = true)) {
            paparazzi.unsafeUpdateConfig(deviceLandscape)
        } else {
            paparazzi.unsafeUpdateConfig(devicePortrait)
        }

        paparazzi.snapshot {
            preview.contentWithUiMode()
        }
    }
}
