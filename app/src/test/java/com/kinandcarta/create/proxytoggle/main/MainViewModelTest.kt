package com.kinandcarta.create.proxytoggle.main

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.repository.userprefs.UserPreferencesRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var mockUserPreferencesRepository: UserPreferencesRepository

    private lateinit var subject: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        every { mockUserPreferencesRepository.isNightMode } returns flowOf(true)
        subject = MainViewModel(mockUserPreferencesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN isNightMode is true THEN useDarkTheme is true`() = runTest {
        // GIVEN
        given(isNightMode = true)

        // THEN
        subject.useDarkTheme.test {
            dispatcher.scheduler.advanceUntilIdle()
            assertThat(expectMostRecentItem()).isTrue()
        }
        verify { mockUserPreferencesRepository.isNightMode }
    }

    @Test
    fun `GIVEN isNightMode is false THEN useDarkTheme is false`() = runTest {
        // GIVEN
        given(isNightMode = false)

        // THEN
        subject.useDarkTheme.test {
            assertThat(expectMostRecentItem()).isFalse()
        }
        verify { mockUserPreferencesRepository.isNightMode }
    }

    private fun given(isNightMode: Boolean) {
        every { mockUserPreferencesRepository.isNightMode } returns flowOf(isNightMode)
    }
}
