package com.kinandcarta.create.proxytoggle.widget.broadcast

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class WidgetProxyUpdateListenerTest {

    @RelaxedMockK
    private lateinit var mockContext: Context

    @MockK
    private lateinit var mockAppWidgetManager: AppWidgetManager

    private val fakeIds = listOf(1, 2, 3).toIntArray()

    private lateinit var subject: WidgetProxyUpdateListener

    private val intent = slot<Intent>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(AppWidgetManager::class)
        every { AppWidgetManager.getInstance(mockContext) } returns mockAppWidgetManager
        every { mockAppWidgetManager.getAppWidgetIds(any()) } returns fakeIds
        every { mockContext.sendBroadcast(capture(intent)) } returns Unit

        subject = WidgetProxyUpdateListener(mockContext)
    }

    @Test
    fun `onProxyUpdate() - build an intent with widget update action`() {
        subject.onProxyUpdate()

        verify {
            val intent = intent.captured
            mockContext.sendBroadcast(intent)
            assertThat(intent.action).isEqualTo(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            assertThat(intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS))
                .isEqualTo(fakeIds)
        }
    }
}
