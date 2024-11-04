package com.example.forcastingapp.map.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forcastingapp.model.Repository
import com.example.forcastingapp.model.SimpleWeatherData
import com.example.forcastingapp.model.WeatherResponse
import com.example.forcastingapp.network.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel(private val repository: Repository) : ViewModel() {

    private val _weatherState = MutableStateFlow<State>(State.Loading)
    val weatherState: StateFlow<State> get() = _weatherState

    private var latestWeatherResponse: WeatherResponse? = null // Store latest response

    fun fetchWeatherData(latitude: Double, longitude: Double, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getWeatherForecast(latitude, longitude, apiKey).collect { weatherResponse ->
                    latestWeatherResponse = weatherResponse // Save response for later use
                    _weatherState.value = State.WeatherSuccess(weatherResponse)
                }
            } catch (e: Exception) {
                Log.e("MapViewModel", "Exception occurred: ${e.message}", e)
                _weatherState.value = State.Error("Exception occurred: ${e.message}")
            }
        }
    }

    fun saveWeatherDataToDatabase() {
        latestWeatherResponse?.let { weatherResponse ->
            val city = weatherResponse.city?.name ?: "Unknown"
            val latitude = weatherResponse.city?.coord?.lat ?: 0.0
            val longitude = weatherResponse.city?.coord?.lon ?: 0.0
            val weather = weatherResponse.list.firstOrNull()?.weather?.firstOrNull()
            val temperature = weatherResponse.list.firstOrNull()?.main?.temp ?: 0.0

            val simpleWeatherData = SimpleWeatherData(
                cityName = city,
                latitude = latitude,
                longitude = longitude,
                weatherIcon = weather?.id ?: 0,
                weatherDescription = weather?.description,
                temp = temperature
            )

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    repository.insertWeather(simpleWeatherData)
                } catch (e: Exception) {
                    Log.e("MapViewModel", "Error adding weather data to database: ${e.message}", e)
                }
            }
        }
    }
}
