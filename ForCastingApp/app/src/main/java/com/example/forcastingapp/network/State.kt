package com.example.forcastingapp.network

import com.example.forcastingapp.model.CurrentWeatherResponse
import com.example.forcastingapp.model.WeatherResponse

sealed class State {
    object Loading : State()
    data class WeatherSuccess(val weatherResponse: WeatherResponse) : State()
    data class CurrWeatherSuccess(val currWeatherResponse: CurrentWeatherResponse) : State()
    data class Error(val message: String) : State()
}