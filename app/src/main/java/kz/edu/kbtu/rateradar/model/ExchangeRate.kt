package kz.edu.kbtu.rateradar.model

data class ExchangeRate(
    val date: String,
    val base: String,
    val quote: String,
    val rate: Double,
)
