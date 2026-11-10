package kz.edu.kbtu.rateradar.presentation

import kz.edu.kbtu.rateradar.domain.model.ExchangeRate

sealed interface UiState {
    object Loading : UiState
    data class Success(val items: List<ExchangeRate>) : UiState
    data class Error(val message: String) : UiState
}
