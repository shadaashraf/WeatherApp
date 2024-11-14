package com.example.forcastingapp.network

import com.example.forcastingapp.model.CurrentWeatherResponse
import com.example.forcastingapp.model.WeatherResponse
import retrofit2.Response

class WeatherRemoteDataSourceImpl private constructor() : WeatherRemoteDataSource {

    private val apiService = RetrofitHelper.getApiService()

    // Function to get weather forecast
    override suspend fun getWeatherForecast(lat: Double, lon: Double, apiKey: String): Response<WeatherResponse> {
        return apiService.getWeatherForecast(lat, lon, apiKey)
    }

    // Function to get current weather forecast
    override suspend fun getCurrWeatherForecast(lat: Double, lon: Double, apiKey: String): Response<CurrentWeatherResponse> {
        return apiService.getCurrentWeather(lat, lon, apiKey)
    }

    companion object {
        @Volatile
        private var INSTANCE: WeatherRemoteDataSourceImpl? = null

        // getInstance method to return a single instance
        fun getInstance(): WeatherRemoteDataSourceImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WeatherRemoteDataSourceImpl().also { INSTANCE = it }
            }
        }
    }
}
