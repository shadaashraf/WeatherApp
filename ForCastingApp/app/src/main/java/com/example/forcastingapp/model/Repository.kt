package com.example.forcastingapp.model

import com.example.forcastingapp.database.LocalRepository
import com.example.forcastingapp.network.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository private constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: WeatherRemoteDataSourceImpl
) {

    // Methods for SimpleWeatherData
    suspend fun insertWeather(simpleWeather: SimpleWeatherData) {
        localRepository.insertWeather(simpleWeather)
    }

    fun getAllWeather(): Flow<List<SimpleWeatherData>> {
        return localRepository.getAllWeather()
    }

    suspend fun deleteWeatherByCity(cityName: String) {
        localRepository.deleteWeatherByCity(cityName)
    }

    // Methods for WeatherAlert
    suspend fun insertWeatherAlert(weatherAlert: WeatherAlert) {
        localRepository.insertWeatherAlert(weatherAlert)
    }

    fun getAllAlerts(): Flow<List<WeatherAlert>> {
        return localRepository.getAllAlerts()
    }

    suspend fun deleteAlertById(id: Int) {
        localRepository.deleteAlertById(id)
    }

    // Remote methods for weather forecast
    fun getWeatherForecast(lat: Double, lon: Double, apiKey: String): Flow<WeatherResponse> = flow {
        val response = remoteRepository.getWeatherForecast(lat, lon, apiKey)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            throw Exception("Failed to fetch weather forecast")
        }
    }

    fun getCurrWeatherForecast(lat: Double, lon: Double, apiKey: String): Flow<CurrentWeatherResponse> = flow {
        val response = remoteRepository.getCurrWeatherForecast(lat, lon, apiKey)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            throw Exception("Failed to fetch current weather"+response.message())
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(localRepository: LocalRepository, remoteRepository: WeatherRemoteDataSourceImpl): Repository {
            return instance ?: synchronized(this) {
                instance ?: Repository(localRepository, remoteRepository).also { instance = it }
            }
        }
    }
}
