package kz.edu.kbtu.rateradar.viewmodel

import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kz.edu.kbtu.rateradar.data.ApiService
import kz.edu.kbtu.rateradar.data.ExchangeOfficeRepository
import kz.edu.kbtu.rateradar.model.ExchangeRate
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
        assertEquals(apiService.rates, viewModel.uiState.value.exchangeRates)
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
        assertEquals(
            "Cannot connect. Check your internet connection and try again.",
            viewModel.uiState.value.error,
        )
        assertTrue(viewModel.uiState.value.exchangeRates.isEmpty())

        apiService.failure = null
        viewModel.onRetry()
        assertTrue(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
        assertEquals(apiService.rates, viewModel.uiState.value.exchangeRates)
    }

    @Test
    fun httpFailureShowsServerMessage() = runTest(dispatcher) {
        apiService.failure = HttpException(
            Response.error<List<ExchangeRate>>(503, ResponseBody.create(null, "Service unavailable")),
        )
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(), apiService)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Server error (HTTP 503). Please try again.", viewModel.uiState.value.error)
        assertTrue(viewModel.uiState.value.exchangeRates.isEmpty())
    }
}

private class FakeApiService : ApiService {
    val rates = listOf(ExchangeRate("2026-10-27", "USD", "KZT", 450.0))
    var failure: Exception? = null

    override suspend fun getRates(): List<ExchangeRate> {
        val exception = failure
        if (exception != null) throw exception
        return rates
    }
}
