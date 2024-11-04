package com.example.forcastingapp.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "simple_weather",
    indices = [Index(value = ["cityName"], unique = true)]
)
data class SimpleWeatherData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val weatherIcon: Int,
    val weatherDescription: String?,
    val temp: Double
)
