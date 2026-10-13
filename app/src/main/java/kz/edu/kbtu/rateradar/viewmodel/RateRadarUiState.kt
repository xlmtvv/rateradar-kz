package kz.edu.kbtu.rateradar.viewmodel

import kz.edu.kbtu.rateradar.model.ExchangeOffice

data class RateRadarUiState(
    val offices: List<ExchangeOffice> = emptyList(),
    val favoriteOffices: List<ExchangeOffice> = emptyList(),
    val favoriteIds: Set<Int> = emptySet(),
    val searchQuery: String = "",
    val selectedCity: String = "All",
    val cities: List<String> = emptyList(),
    val selectedOfficeId: Int? = null,
    val selectedOffice: ExchangeOffice? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
)
