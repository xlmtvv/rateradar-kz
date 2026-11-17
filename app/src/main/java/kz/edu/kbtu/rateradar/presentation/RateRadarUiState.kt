package kz.edu.kbtu.rateradar.presentation

import kz.edu.kbtu.rateradar.domain.model.ExchangeOffice
import kz.edu.kbtu.rateradar.domain.model.ExchangeRate

enum class RateReminderStatus {
    IDLE, WAITING, LOADING, ERROR,
}

data class RateRadarUiState(
    val exchangeRates: List<ExchangeRate> = emptyList(),
    val offices: List<ExchangeOffice> = emptyList(),
    val favoriteOffices: List<ExchangeOffice> = emptyList(),
    val favoriteIds: Set<Int> = emptySet(),
    val searchQuery: String = "",
    val selectedCity: String = "All",
    val cities: List<String> = emptyList(),
    val selectedOfficeId: Int? = null,
    val selectedOffice: ExchangeOffice? = null,
    val isLoading: Boolean = true,
    val error: Int? = null,
    val httpErrorCode: Int? = null,
    val rateReminderStatus: RateReminderStatus = RateReminderStatus.IDLE,
) {
    val isRateReminderRunning: Boolean
        get() = rateReminderStatus == RateReminderStatus.WAITING || rateReminderStatus == RateReminderStatus.LOADING
}
