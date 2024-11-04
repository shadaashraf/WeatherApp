package com.example.forcastingapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forcastingapp.model.SimpleWeatherData


import kotlinx.coroutines.flow.Flow

@Dao
interface favWeatherDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertWeather(simpleWeather: SimpleWeatherData)

        // Use Flow to observe changes in the weather data
        @Query("SELECT * FROM simple_weather")
        fun getAllFav(): Flow<List<SimpleWeatherData>>

        @Query("DELETE FROM simple_weather WHERE cityName = :cityName")
        suspend fun deleteWeatherByCity(cityName: String)
}
