package com.kinandcarta.create.proxytoggle.repository.appdata

import android.app.Application
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.kinandcarta.create.proxytoggle.core.common.proxy.Proxy
import com.kinandcarta.create.proxytoggle.datastore.AppData
import com.kinandcarta.create.proxytoggle.datastore.PastProxy
import com.kinandcarta.create.proxytoggle.repository.datastore.AppDataSerializer
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class AppDataRepositoryImplTest {

    private lateinit var testDataStore: DataStore<AppData>

    private lateinit var subject: AppDataRepositoryImpl

    private val context = ApplicationProvider.getApplicationContext<Application>()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this)

        testDataStore = DataStoreFactory.create(
            serializer = AppDataSerializer,
            produceFile = { context.dataStoreFile("fake_app_data.pb") }
        )

        subject = AppDataRepositoryImpl(testDataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN 0 past proxies, WHEN saveProxy(), THEN pastProxies returns that proxy`() = runTest {
        val newProxy = testProxies.first().toProxy()
        subject.pastProxies.test {
            subject.saveProxy(newProxy)
            assertThat(expectMostRecentItem()).isEqualTo(listOf(newProxy))
        }
    }

    @Test
    fun `GIVEN 4 past proxies, WHEN I save new proxy, THEN pastProxies = previous 4 + new, time-sorted`() {
        runTest {
            val pastProxies = testProxies.take(4)
            val newProxy = testProxies.last().toProxy()
            subject.pastProxies.test {
                testDataStore.updateData { appDataWithProxies(pastProxies) }
                subject.saveProxy(newProxy)
                assertThat(expectMostRecentItem()).isEqualTo(
                    listOf(newProxy) +
                        pastProxies.sortedByDescending { it.timestamp }.map { it.toProxy() }
                )
            }
        }
    }

    @Test
    fun `GIVEN 5 past proxies, WHEN I save new proxy, THEN pastProxies = 4 most recent previous + new, time-sorted`() {
        runTest {
            val newProxy = Proxy("42.42.42.42", "42")
            subject.pastProxies.test {
                testDataStore.updateData { appDataWithProxies(testProxies) }
                subject.saveProxy(newProxy)
                assertThat(expectMostRecentItem()).isEqualTo(
                    listOf(newProxy) +
                        testProxies.sortedByDescending { it.timestamp }.take(4).map { it.toProxy() }
                )
            }
        }
    }

    @Test
    fun `GIVEN 5 past proxies, WHEN I save existing proxy, THEN pastProxies = all previous, time-sorted`() {
        runTest {
            val newProxy = testProxies.last().toProxy()
            subject.pastProxies.test {
                testDataStore.updateData { appDataWithProxies(testProxies) }
                subject.saveProxy(newProxy)
                assertThat(expectMostRecentItem()).isEqualTo(
                    listOf(newProxy) +
                        testProxies.take(4).sortedByDescending { it.timestamp }.map { it.toProxy() }
                )
            }
        }
    }

    private fun appDataWithProxies(proxies: List<PastProxy>): AppData {
        return AppData.newBuilder()
            .addAllPastProxies(proxies.toMutableList())
            .build()
    }

    private val yesterday = Instant.now().minus(1, ChronoUnit.DAYS).epochSecond

    private val testProxies = listOf(
        pastProxy("1.1.1.1", "1", yesterday - 1),
        pastProxy("2.2.2.2", "2", yesterday + 2),
        pastProxy("3.3.3.3", "3", yesterday - 3),
        pastProxy("4.4.4.4", "4", yesterday + 4),
        pastProxy("5.5.5.5", "5", yesterday - 5)
    )

    private fun pastProxy(address: String, port: String, epochSecond: Long): PastProxy {
        return PastProxy.newBuilder()
            .setAddress(address)
            .setPort(port)
            .setTimestamp(epochSecond)
            .build()
    }

    private fun PastProxy.toProxy(): Proxy {
        return Proxy(this.address, this.port)
    }
}
