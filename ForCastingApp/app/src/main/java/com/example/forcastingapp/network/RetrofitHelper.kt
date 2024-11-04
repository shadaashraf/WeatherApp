package com.example.forcastingapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getApiService(): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }
}
