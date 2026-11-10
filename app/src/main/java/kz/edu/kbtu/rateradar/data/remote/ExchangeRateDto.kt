package kz.edu.kbtu.rateradar.data.remote

data class ExchangeRateDto(
    val date: String,
    val base: String,
    val quote: String,
    val rate: Double,
)
