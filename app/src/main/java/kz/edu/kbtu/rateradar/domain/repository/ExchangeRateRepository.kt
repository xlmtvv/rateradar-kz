package kz.edu.kbtu.rateradar.domain.repository

import kz.edu.kbtu.rateradar.domain.model.ExchangeRate

interface ExchangeRateRepository {
    suspend fun getItems(): List<ExchangeRate>
}
