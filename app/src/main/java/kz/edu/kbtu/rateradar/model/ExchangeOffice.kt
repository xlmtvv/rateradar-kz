package kz.edu.kbtu.rateradar.model

data class ExchangeOffice(
    val id: Int,
    val name: String,
    val city: String,
    val address: String,
    val workingHours: String,
    val usdBuy: Double,
    val usdSell: Double,
    val updatedAt: String,
)
