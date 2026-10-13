package kz.edu.kbtu.rateradar.viewmodel

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
import kz.edu.kbtu.rateradar.data.ExchangeOfficeRepository
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RateRadarViewModelTest {
    private val dispatcher = StandardTestDispatcher()

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
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository())
        assertTrue(viewModel.uiState.value.isLoading)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
        assertEquals(12, viewModel.uiState.value.offices.size)
    }

    @Test
    fun searchAndCityFilterWorkTogether() = runTest(dispatcher) {
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository())
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
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository())
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
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository())
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
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository())
        advanceUntilIdle()
        viewModel.onFavoriteClick(999)
        viewModel.onOfficeSelected(999)
        assertTrue(viewModel.uiState.value.favoriteIds.isEmpty())
        assertNull(viewModel.uiState.value.selectedOffice)
    }

    @Test
    fun simulatedErrorCanBeRetriedWithoutLosingQueryOrCity() = runTest(dispatcher) {
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository(shouldFail = true))
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
        val viewModel = RateRadarViewModel(ExchangeOfficeRepository())
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
}
