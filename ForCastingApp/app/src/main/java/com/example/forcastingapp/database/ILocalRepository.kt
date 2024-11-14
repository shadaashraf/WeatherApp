package com.example.forcastingapp.database


import com.example.forcastingapp.model.SimpleWeatherData
import com.example.forcastingapp.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

interface ILocalRepository {

    // Methods for SimpleWeatherData
    suspend fun insertWeather(simpleWeather: SimpleWeatherData)
    fun getAllWeather(): Flow<List<SimpleWeatherData>>
    suspend fun deleteWeatherByCity(cityName: String)

    // Methods for WeatherAlert
    suspend fun insertWeatherAlert(alert: WeatherAlert)
    fun getAllAlerts(): Flow<List<WeatherAlert>>
    suspend fun deleteAlertById(id: Int)
}
