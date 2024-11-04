package com.example.forcastingapp.network

import com.example.forcastingapp.model.CurrentWeatherResponse
import com.example.forcastingapp.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
interface WeatherService {

    // Fetches the weather forecast
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String
    ): Response<WeatherResponse>

    // Fetches the current weather data
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String
    ): Response<CurrentWeatherResponse>
}
