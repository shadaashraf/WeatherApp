package com.example.forcastingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "weather_alerts")
data class WeatherAlert(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val duration: Long,
    val alarmType: Int,
    val cityName: String // Optional: store which city's alert this belongs to
)