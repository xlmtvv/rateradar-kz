package kz.edu.kbtu.rateradar.domain.model

data class ExchangeRate(
    val date: String,
    val base: String,
    val quote: String,
    val rate: Double,
)
