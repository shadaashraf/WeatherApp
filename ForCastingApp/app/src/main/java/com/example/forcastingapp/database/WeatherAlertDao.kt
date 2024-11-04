package com.example.forcastingapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.forcastingapp.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherAlertDao {
    @Insert
    suspend fun insertAlert(weatherAlert: WeatherAlert)

    @Query("SELECT * FROM weather_alerts")
    fun getAllAlerts(): Flow<List<WeatherAlert>>

    @Query("DELETE FROM weather_alerts WHERE id = :alertId")
    suspend fun deleteAlert(alertId: Int)
}