package com.kinandcarta.create.proxytoggle.manager.view.composable

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL
import app.cash.paparazzi.Paparazzi
import com.airbnb.android.showkase.models.Showkase
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.kinandcarta.create.proxytoggle.manager.getMetadata
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class ComposeScreenshotTests {

    object PreviewProvider : TestParameter.TestParameterValuesProvider {
        override fun provideValues(): List<ComponentPreview> {
            return Showkase.getMetadata().componentList.map(::ComponentPreview).filter {
                it.group.contains("landscape", ignoreCase = true).not()
            }
        }
    }

    @get:Rule
    val paparazzi = Paparazzi(
        maxPercentDifference = 0.0,
        deviceConfig = PIXEL.copy(softButtons = false)
    )

    @Test
    fun preview_tests(
        @TestParameter(valuesProvider = PreviewProvider::class) preview: ComponentPreview
    ) {
        paparazzi.snapshot {
            preview.content()
        }
    }
}
