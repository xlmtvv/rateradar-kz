package kz.edu.kbtu.rateradar.data.remote

import retrofit2.http.GET

interface ApiService {
    @GET("v2/rates?base=USD&quotes=KZT")
    suspend fun getRates(): List<ExchangeRateDto>
}
