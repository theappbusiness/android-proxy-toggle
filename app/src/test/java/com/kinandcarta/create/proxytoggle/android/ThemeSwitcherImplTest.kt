package com.kinandcarta.create.proxytoggle.android

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.core.settings.AppSettings
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ThemeSwitcherImplTest {

    companion object {
        private const val DARK_MODE = AppCompatDelegate.MODE_NIGHT_YES
        private const val LIGHT_MODE = AppCompatDelegate.MODE_NIGHT_NO
        private const val NO_MODE_SELECTED = AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
    }

    @RelaxedMockK
    private lateinit var mockContext: Context

    @MockK
    private lateinit var mockAppSettings: AppSettings

    private lateinit var subject: ThemeSwitcherImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        subject = ThemeSwitcherImpl(mockContext, mockAppSettings)
    }

    @Test
    fun `init - GIVEN I have dark mode selected THEN dark mode is set`() {
        every { mockAppSettings.themeMode } returns DARK_MODE

        subject = ThemeSwitcherImpl(mockContext, mockAppSettings)

        verify { mockAppSettings.themeMode = DARK_MODE }
    }

    @Test
    fun `init - GIVEN I have light mode selected THEN light mode is set`() {
        every { mockAppSettings.themeMode } returns LIGHT_MODE

        subject = ThemeSwitcherImpl(mockContext, mockAppSettings)

        verify { mockAppSettings.themeMode = LIGHT_MODE }
    }

    @Test
    fun `init - GIVEN I have no mode selected AND dark configuration THEN dark mode is set`() {
        every { mockAppSettings.themeMode } returns NO_MODE_SELECTED
        every { mockContext.resources } returns mockk {
            every { configuration } returns mockk {
                uiMode = Configuration.UI_MODE_NIGHT_YES
            }
        }

        subject = ThemeSwitcherImpl(mockContext, mockAppSettings)

        verify { mockAppSettings.themeMode = DARK_MODE }
    }

    @Test
    fun `init - GIVEN I have no mode selected AND light configuration THEN light mode is set`() {
        every { mockAppSettings.themeMode } returns NO_MODE_SELECTED
        every { mockContext.resources } returns mockk {
            every { configuration } returns mockk {
                uiMode = Configuration.UI_MODE_NIGHT_NO
            }
        }

        subject = ThemeSwitcherImpl(mockContext, mockAppSettings)

        verify { mockAppSettings.themeMode = LIGHT_MODE }
    }

    @Test
    fun `init - GIVEN I have no mode selected AND no configuration THEN light mode is set`() {
        every { mockAppSettings.themeMode } returns NO_MODE_SELECTED
        every { mockContext.resources } returns mockk {
            every { configuration } returns mockk {
                uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED
            }
        }

        subject = ThemeSwitcherImpl(mockContext, mockAppSettings)

        verify { mockAppSettings.themeMode = LIGHT_MODE }
    }

    @Test
    fun `toggleTheme() - GIVEN I have dark mode THEN light mode is enabled AND stored in settings`() {
        every { mockAppSettings.themeMode } returns DARK_MODE

        subject.toggleTheme()

        verify { mockAppSettings.themeMode = LIGHT_MODE }
    }

    @Test
    fun `toggleTheme() - GIVEN I have light mode THEN dark mode is enabled AND stored in settings`() {
        every { mockAppSettings.themeMode } returns LIGHT_MODE

        subject.toggleTheme()

        verify { mockAppSettings.themeMode = DARK_MODE }
    }

    @Test
    fun `isNightMode() - GIVEN settings light mode THEN isNightMode returns false`() {
        every { mockAppSettings.themeMode } returns LIGHT_MODE

        assertThat(subject.isNightMode()).isEqualTo(false)
    }

    @Test
    fun `isNightMode() - GIVEN settings dark mode THEN isNightMode returns true`() {
        every { mockAppSettings.themeMode } returns DARK_MODE

        assertThat(subject.isNightMode()).isEqualTo(true)
    }
}
