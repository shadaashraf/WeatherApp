package com.example.forcastingapp.model

import kotlinx.coroutines.flow.Flow

interface IRepository {

    // Methods for SimpleWeatherData
    suspend fun insertWeather(simpleWeather: SimpleWeatherData)
    fun getAllWeather(): Flow<List<SimpleWeatherData>>
    suspend fun deleteWeatherByCity(cityName: String)

    // Methods for WeatherAlert
    suspend fun insertWeatherAlert(weatherAlert: WeatherAlert)
    fun getAllAlerts(): Flow<List<WeatherAlert>>
    suspend fun deleteAlertById(id: Int)

    // Remote methods for weather forecast
    fun getWeatherForecast(lat: Double, lon: Double, apiKey: String): Flow<WeatherResponse>
    fun getCurrWeatherForecast(lat: Double, lon: Double, apiKey: String): Flow<CurrentWeatherResponse>
}
