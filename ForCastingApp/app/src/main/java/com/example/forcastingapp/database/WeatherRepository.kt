package com.example.forcastingapp.database

import com.example.forcastingapp.database.WeatherAlertDao
import com.example.forcastingapp.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

class WeatherRepository(private val weatherAlertDao: WeatherAlertDao) {

    fun getAllAlerts(): Flow<List<WeatherAlert>> {
        return weatherAlertDao.getAllAlerts()
    }

    suspend fun insertAlert(weatherAlert: WeatherAlert) {
        weatherAlertDao.insertAlert(weatherAlert)
    }

    suspend fun deleteAlert(alertId: Int) {
        weatherAlertDao.deleteAlert(alertId)
    }
}
