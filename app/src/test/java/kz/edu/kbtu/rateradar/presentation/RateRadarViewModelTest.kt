package kz.edu.kbtu.rateradar.presentation

import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kz.edu.kbtu.rateradar.R
import kz.edu.kbtu.rateradar.data.remote.ApiService
import kz.edu.kbtu.rateradar.data.remote.ExchangeRateDto
import kz.edu.kbtu.rateradar.data.repository.ExchangeOfficeRepository
import kz.edu.kbtu.rateradar.domain.model.ExchangeRate
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class RateRadarViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val apiService = FakeApiService()
    private val expectedRates = listOf(ExchangeRate("2026-10-27", "USD", "KZT", 450.0))

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun repositoryWaitsBeforeReturningTwelveUniqueOffices() = runTest(dispatcher) {
        val request = async { ExchangeOfficeRepository().loadOffices() }
        runCurrent()
        assertFalse(request.isCompleted)
        advanceTimeBy(999)
        runCurrent()
        assertFalse(request.isCompleted)
        advanceTimeBy(1)
        runCurrent()
        val offices = request.await()
        assertEquals(12, offices.size)
        assertEquals(12, offices.map { it.id }.toSet().size)
    }

    @Test
    fun initialLoadChangesLoadingToContent() = runTest(dispatcher) {
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(), apiService)
        assertTrue(viewModel.uiState.value.isLoading)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
        assertEquals(12, viewModel.uiState.value.offices.size)
        assertEquals(expectedRates, viewModel.uiState.value.exchangeRates)
    }

    @Test
    fun searchAndCityFilterWorkTogether() = runTest(dispatcher) {
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(), apiService)
        advanceUntilIdle()
        viewModel.onSearchChange("  currency  ")
        viewModel.onCitySelected("Astana")
        assertEquals(listOf(7), viewModel.uiState.value.offices.map { it.id })
        assertEquals("  currency  ", viewModel.uiState.value.searchQuery)
        viewModel.onSearchChange("NURZHOL")
        assertEquals(listOf(6), viewModel.uiState.value.offices.map { it.id })
    }

    @Test
    fun noMatchesProducesEmptyContentWithoutAnError() = runTest(dispatcher) {
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(), apiService)
        advanceUntilIdle()
        viewModel.onCitySelected("Karaganda")
        assertTrue(viewModel.uiState.value.offices.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
        viewModel.onCitySelected("All")
        viewModel.onSearchChange("no such office")
        assertTrue(viewModel.uiState.value.offices.isEmpty())
    }

    @Test
    fun favoritesAndDetailsUseAllDataRatherThanFilteredItems() = runTest(dispatcher) {
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(), apiService)
        advanceUntilIdle()
        viewModel.onFavoriteClick(1)
        viewModel.onCitySelected("Astana")
        viewModel.onSearchChange("Khan")
        viewModel.onOfficeSelected(1)
        assertEquals(setOf(1), viewModel.uiState.value.favoriteIds)
        assertEquals(listOf(1), viewModel.uiState.value.favoriteOffices.map { it.id })
        assertEquals("Abay Exchange", viewModel.uiState.value.selectedOffice?.name)
        viewModel.onFavoriteClick(1)
        assertTrue(viewModel.uiState.value.favoriteIds.isEmpty())
        assertTrue(viewModel.uiState.value.favoriteOffices.isEmpty())
    }

    @Test
    fun unknownFavoriteIdIsIgnoredAndUnknownDetailsIsEmpty() = runTest(dispatcher) {
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(), apiService)
        advanceUntilIdle()
        viewModel.onFavoriteClick(999)
        viewModel.onOfficeSelected(999)
        assertTrue(viewModel.uiState.value.favoriteIds.isEmpty())
        assertNull(viewModel.uiState.value.selectedOffice)
    }

    @Test
    fun simulatedErrorCanBeRetriedWithoutLosingQueryOrCity() = runTest(dispatcher) {
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(shouldFail = true), apiService)
        viewModel.onSearchChange("Baiterek")
        viewModel.onCitySelected("Astana")
        advanceUntilIdle()
        assertNotNull(viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.isLoading)
        viewModel.onRetry()
        assertTrue(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
        advanceUntilIdle()
        assertEquals(listOf(6), viewModel.uiState.value.offices.map { it.id })
        assertEquals("Baiterek", viewModel.uiState.value.searchQuery)
        assertEquals("Astana", viewModel.uiState.value.selectedCity)
    }

    @Test
    fun reloadKeepsFavoriteIdsAndSelectedDetails() = runTest(dispatcher) {
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(), apiService)
        advanceUntilIdle()
        viewModel.onFavoriteClick(6)
        viewModel.onOfficeSelected(6)
        viewModel.onRetry()
        viewModel.onRetry()
        advanceUntilIdle()
        assertEquals(setOf(6), viewModel.uiState.value.favoriteIds)
        assertEquals(6, viewModel.uiState.value.selectedOffice?.id)
        assertEquals(listOf(6), viewModel.uiState.value.favoriteOffices.map { it.id })
    }

    @Test
    fun networkFailureShowsMessageAndRetryLoadsRates() = runTest(dispatcher) {
        apiService.failure = IOException("Network unavailable")
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(), apiService)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(R.string.error_network, viewModel.uiState.value.error)
        assertTrue(viewModel.uiState.value.exchangeRates.isEmpty())

        apiService.failure = null
        viewModel.onRetry()
        assertTrue(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
        assertNull(viewModel.uiState.value.httpErrorCode)
        assertEquals(expectedRates, viewModel.uiState.value.exchangeRates)
    }

    @Test
    fun httpFailureShowsServerMessage() = runTest(dispatcher) {
        apiService.failure = HttpException(
            Response.error<List<ExchangeRateDto>>(503, ResponseBody.create(null, "Service unavailable")),
        )
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(), apiService)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(R.string.error_server, viewModel.uiState.value.error)
        assertEquals(503, viewModel.uiState.value.httpErrorCode)
        assertTrue(viewModel.uiState.value.exchangeRates.isEmpty())

        apiService.failure = null
        viewModel.onRetry()
        assertNull(viewModel.uiState.value.error)
        assertNull(viewModel.uiState.value.httpErrorCode)
        advanceUntilIdle()
        assertEquals(expectedRates, viewModel.uiState.value.exchangeRates)
    }

    @Test
    fun rateReminderFiresOnceAfterTenSeconds() = runTest(dispatcher) {
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(), apiService)
        var notifications = 0

        viewModel.startRateReminder { notifications++ }
        viewModel.startRateReminder { notifications++ }
        assertTrue(viewModel.uiState.value.isRateReminderRunning)

        advanceTimeBy(9_999)
        assertEquals(0, notifications)
        assertTrue(viewModel.uiState.value.isRateReminderRunning)

        advanceTimeBy(1)
        runCurrent()
        assertEquals(1, notifications)
        assertEquals(2, apiService.rateRequests)
        assertFalse(viewModel.uiState.value.isRateReminderRunning)
    }

    @Test
    fun canceledRateReminderDoesNotNotify() = runTest(dispatcher) {
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(), apiService)
        var notifications = 0

        viewModel.startRateReminder { notifications++ }
        advanceTimeBy(1_000)
        viewModel.cancelRateReminder()
        advanceUntilIdle()

        assertEquals(0, notifications)
        assertEquals(1, apiService.rateRequests)
        assertFalse(viewModel.uiState.value.isRateReminderRunning)
    }

    @Test
    fun rateReminderRetriesFailedRequestWithoutWaitingAgain() = runTest(dispatcher) {
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(), apiService)
        advanceUntilIdle()
        apiService.responseDelay = 500
        apiService.failure = IOException("No connection")
        var notifiedRate: ExchangeRate? = null

        viewModel.startRateReminder { notifiedRate = it }
        advanceTimeBy(10_000)
        runCurrent()
        assertEquals(RateReminderStatus.LOADING, viewModel.uiState.value.rateReminderStatus)
        advanceTimeBy(500)
        runCurrent()
        assertEquals(RateReminderStatus.ERROR, viewModel.uiState.value.rateReminderStatus)
        assertNull(notifiedRate)

        apiService.failure = null
        viewModel.startRateReminder { notifiedRate = it }
        assertEquals(RateReminderStatus.LOADING, viewModel.uiState.value.rateReminderStatus)
        advanceTimeBy(500)
        runCurrent()
        assertEquals(expectedRates.first(), notifiedRate)
        assertEquals(RateReminderStatus.IDLE, viewModel.uiState.value.rateReminderStatus)
    }
}

private class FakeApiService : ApiService {
    val rates = listOf(ExchangeRateDto("2026-10-27", "USD", "KZT", 450.0))
    var failure: Exception? = null
    var responseDelay = 0L
    var rateRequests = 0

    override suspend fun getRates(): List<ExchangeRateDto> {
        rateRequests++
        if (responseDelay > 0) delay(responseDelay)
        val exception = failure
        if (exception != null) throw exception
        return rates
    }
}
