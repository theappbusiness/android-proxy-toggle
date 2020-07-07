package com.kinandcarta.create.proxytoggle.android

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.kinandcarta.create.proxytoggle.settings.AppSettings
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ThemeSwitcherTest {

    companion object {
        private const val DARK_MODE = AppCompatDelegate.MODE_NIGHT_YES
        private const val LIGHT_MODE = AppCompatDelegate.MODE_NIGHT_NO
        private const val NO_MODE_SELECTED = AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
    }

    @RelaxedMockK
    private lateinit var mockContext: Context

    @MockK
    private lateinit var mockAppSettings: AppSettings

    private lateinit var subject: ThemeSwitcher

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        mockkStatic(AppCompatDelegate::class)

        subject = ThemeSwitcher(mockContext, mockAppSettings)
    }

    @Test
    fun `init - GIVEN I have dark mode selected THEN dark mode is set`() {
        every { mockAppSettings.themeMode } returns DARK_MODE

        subject = ThemeSwitcher(mockContext, mockAppSettings)

        verify {
            mockAppSettings.themeMode = DARK_MODE
            AppCompatDelegate.setDefaultNightMode(DARK_MODE)
        }
    }

    @Test
    fun `init - GIVEN I have light mode selected THEN light mode is set`() {
        every { mockAppSettings.themeMode } returns LIGHT_MODE

        subject = ThemeSwitcher(mockContext, mockAppSettings)

        verify {
            mockAppSettings.themeMode = LIGHT_MODE
            AppCompatDelegate.setDefaultNightMode(LIGHT_MODE)
        }
    }

    @Test
    fun `init - GIVEN I have no mode selected AND dark configuration THEN dark mode is set`() {
        every { mockAppSettings.themeMode } returns NO_MODE_SELECTED
        every { mockContext.resources } returns mockk {
            every { configuration } returns mockk {
                uiMode = Configuration.UI_MODE_NIGHT_YES
            }
        }

        subject = ThemeSwitcher(mockContext, mockAppSettings)

        verify {
            mockAppSettings.themeMode = DARK_MODE
            AppCompatDelegate.setDefaultNightMode(DARK_MODE)
        }
    }

    @Test
    fun `init - GIVEN I have no mode selected AND light configuration THEN light mode is set`() {
        every { mockAppSettings.themeMode } returns NO_MODE_SELECTED
        every { mockContext.resources } returns mockk {
            every { configuration } returns mockk {
                uiMode = Configuration.UI_MODE_NIGHT_NO
            }
        }

        subject = ThemeSwitcher(mockContext, mockAppSettings)

        verify {
            mockAppSettings.themeMode = LIGHT_MODE
            AppCompatDelegate.setDefaultNightMode(LIGHT_MODE)
        }
    }

    @Test
    fun `init - GIVEN I have no mode selected AND no configuration THEN light mode is set`() {
        every { mockAppSettings.themeMode } returns NO_MODE_SELECTED
        every { mockContext.resources } returns mockk {
            every { configuration } returns mockk {
                uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED
            }
        }

        subject = ThemeSwitcher(mockContext, mockAppSettings)

        verify {
            mockAppSettings.themeMode = LIGHT_MODE
            AppCompatDelegate.setDefaultNightMode(LIGHT_MODE)
        }
    }

    @Test
    fun `toggleTheme() - GIVEN I have dark mode THEN light mode is enabled AND stored in settings`() {
        every { mockAppSettings.themeMode } returns DARK_MODE

        subject.toggleTheme()

        verify {
            mockAppSettings.themeMode = LIGHT_MODE
            AppCompatDelegate.setDefaultNightMode(LIGHT_MODE)
        }
    }

    @Test
    fun `toggleTheme() - GIVEN I have light mode THEN dark mode is enabled AND stored in settings`() {
        every { mockAppSettings.themeMode } returns LIGHT_MODE

        subject.toggleTheme()

        verify {
            mockAppSettings.themeMode = DARK_MODE
            AppCompatDelegate.setDefaultNightMode(DARK_MODE)
        }
    }
}
