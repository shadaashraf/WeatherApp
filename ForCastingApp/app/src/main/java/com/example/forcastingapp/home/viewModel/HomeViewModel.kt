package com.example.forcastingapp.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forcastingapp.model.CurrentWeatherResponse
import com.example.forcastingapp.model.Repository
import com.example.forcastingapp.model.WeatherResponse
import com.example.forcastingapp.network.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val _weatherData = MutableStateFlow<State>(State.Loading)
    val weatherData: StateFlow<State> get() = _weatherData

    private val _currWeatherData = MutableStateFlow<State>(State.Loading)
    val currWeatherData: StateFlow<State> get() = _currWeatherData

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> get() = _errorState

    // Fetching weather forecast data
    fun fetchWeatherData(latitude: Double, longitude: Double, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getWeatherForecast(latitude, longitude, apiKey).collect { weatherResponse ->
                    _weatherData.value = State.WeatherSuccess(weatherResponse)
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception occurred: ${e.message}", e)
                _weatherData.value = State.Error("Exception occurred: ${e.message}")
            }
        }
    }

    // Fetching current weather data
    fun fetchCurrentWeatherData(latitude: Double, longitude: Double, apiKey: String) {
        _currWeatherData.value = State.Loading // Set loading state
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getCurrWeatherForecast(latitude, longitude, apiKey).collect { currentWeatherResponse ->
                    _currWeatherData.value = State.CurrWeatherSuccess(currentWeatherResponse)
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception occurred: ${e.message}", e)
                _currWeatherData.value = State.Error("Exception occurred: ${e.message}")
            }
        }
    }
}
