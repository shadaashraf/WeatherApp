package com.example.forcastingapp


import com.example.forcastingapp.model.CurrentWeatherResponse
import com.example.forcastingapp.model.WeatherResponse
import com.example.forcastingapp.network.WeatherRemoteDataSource
import retrofit2.Response

class FakeRemoteData : WeatherRemoteDataSource {

    // Mocking the response for weather forecast
    override suspend fun getWeatherForecast(lat: Double, lon: Double, apiKey: String): Response<WeatherResponse> {
        TODO("Not yet implemented")
    }

    // Mocking the response for current weather forecast
    override suspend fun getCurrWeatherForecast(lat: Double, lon: Double, apiKey: String): Response<CurrentWeatherResponse> {
        TODO("Not yet implemented")
    }
}
