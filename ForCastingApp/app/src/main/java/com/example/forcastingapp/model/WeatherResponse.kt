package com.example.forcastingapp.model

data class WeatherResponse(
    val cod: String?,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherList>,
    val city: City?
)
