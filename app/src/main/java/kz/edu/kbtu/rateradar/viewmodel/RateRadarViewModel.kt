package kz.edu.kbtu.rateradar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import java.io.IOException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kz.edu.kbtu.rateradar.BuildConfig
import kz.edu.kbtu.rateradar.data.ExchangeOfficeRepository
import kz.edu.kbtu.rateradar.model.ExchangeOffice

class RateRadarViewModel(private val repository: ExchangeOfficeRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RateRadarUiState(cities = repository.cities))
    val uiState: StateFlow<RateRadarUiState> = _uiState.asStateFlow()

    private var allOffices: List<ExchangeOffice> = emptyList()
    private var loadJob: Job? = null

    init {
        Log.d("RateRadarViewModel", "init instance=${System.identityHashCode(this)}")
        onRetry()
    }

    fun onSearchChange(query: String) {
        _uiState.update { state -> updatedState(state.copy(searchQuery = query)) }
    }

    fun onCitySelected(city: String) {
        if (city !in repository.cities) return
        _uiState.update { state -> updatedState(state.copy(selectedCity = city)) }
    }

    fun onFavoriteClick(id: Int) {
        if (allOffices.none { it.id == id }) return
        _uiState.update { state ->
            val ids = if (id in state.favoriteIds) state.favoriteIds - id else state.favoriteIds + id
            updatedState(state.copy(favoriteIds = ids))
        }
    }

    fun onOfficeSelected(id: Int) {
        _uiState.update { state -> updatedState(state.copy(selectedOfficeId = id)) }
    }

    fun onRetry() {
        if (loadJob?.isActive == true) return
        _uiState.update { it.copy(isLoading = true, error = null) }
        loadJob = viewModelScope.launch {
            try {
                allOffices = repository.loadOffices()
                _uiState.update { state -> updatedState(state.copy(isLoading = false, error = null)) }
            } catch (exception: IOException) {
                _uiState.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }

    private fun updatedState(state: RateRadarUiState): RateRadarUiState {
        val query = state.searchQuery.trim()
        val offices = allOffices.filter { office ->
            val matchesCity = state.selectedCity == ExchangeOfficeRepository.ALL_CITIES ||
                office.city == state.selectedCity
            val matchesQuery = office.name.contains(query, ignoreCase = true) ||
                office.city.contains(query, ignoreCase = true) ||
                office.address.contains(query, ignoreCase = true)
            matchesCity && matchesQuery
        }
        return state.copy(
            offices = offices,
            favoriteOffices = allOffices.filter { it.id in state.favoriteIds },
            selectedOffice = allOffices.find { it.id == state.selectedOfficeId },
        )
    }

    override fun onCleared() {
        Log.d("RateRadarViewModel", "onCleared instance=${System.identityHashCode(this)}")
        super.onCleared()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                RateRadarViewModel(ExchangeOfficeRepository(shouldFail = BuildConfig.SIMULATE_LOAD_ERROR))
            }
        }
    }
}
