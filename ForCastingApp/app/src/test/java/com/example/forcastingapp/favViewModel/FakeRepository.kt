package com.example.forcastingapp.database

import com.example.forcastingapp.model.Clouds
import com.example.forcastingapp.model.Coord
import com.example.forcastingapp.model.IRepository
import com.example.forcastingapp.model.SimpleWeatherData
import com.example.forcastingapp.model.WeatherAlert
import com.example.forcastingapp.model.WeatherResponse
import com.example.forcastingapp.model.CurrentWeatherResponse
import com.example.forcastingapp.model.Main
import com.example.forcastingapp.model.Wind
import com.example.forcastingapp.model.currSys
import com.example.forcastingapp.model.currWeatherList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeRepository : IRepository {

    private val weatherList = mutableListOf<SimpleWeatherData>()
    private val weatherAlertList = mutableListOf<WeatherAlert>()

    // StateFlow to simulate LiveData
    private val _weatherFlow = MutableStateFlow<List<SimpleWeatherData>>(emptyList())
    private val _alertFlow = MutableStateFlow<List<WeatherAlert>>(emptyList())

    // Weather methods
    override suspend fun insertWeather(simpleWeather: SimpleWeatherData) {
        weatherList.add(simpleWeather)
        _weatherFlow.value = weatherList // Update the flow
    }

    override fun getAllWeather(): Flow<List<SimpleWeatherData>> {
        return _weatherFlow // Return the flow with weather data
    }

    override suspend fun deleteWeatherByCity(cityName: String) {
        weatherList.removeAll { it.cityName == cityName }
        _weatherFlow.value = weatherList // Update the flow after removal
    }

    // Weather Alert methods
    override suspend fun insertWeatherAlert(weatherAlert: WeatherAlert) {
        weatherAlertList.add(weatherAlert)
        _alertFlow.value = weatherAlertList // Update the flow
    }

    override fun getAllAlerts(): Flow<List<WeatherAlert>> {
        return _alertFlow // Return the flow with alerts
    }

    override suspend fun deleteAlertById(id: Int) {
        weatherAlertList.removeAll { it.id == id }
        _alertFlow.value = weatherAlertList // Update the flow after removal
    }

    // Remote weather forecast methods - no-op since you don't want to test these
    override fun getWeatherForecast(lat: Double, lon: Double, apiKey: String): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override fun getCurrWeatherForecast(lat: Double, lon: Double, apiKey: String): Flow<CurrentWeatherResponse> {
        TODO("Not yet implemented")
    }
}
