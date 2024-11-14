package com.example.forcastingapp.network

import com.example.forcastingapp.model.CurrentWeatherResponse
import com.example.forcastingapp.model.WeatherResponse
import retrofit2.Response

interface WeatherRemoteDataSource {

    // Function to get weather forecast
    suspend fun getWeatherForecast(lat: Double, lon: Double, apiKey: String): Response<WeatherResponse>

    // Function to get current weather forecast
    suspend fun getCurrWeatherForecast(lat: Double, lon: Double, apiKey: String): Response<CurrentWeatherResponse>
}
