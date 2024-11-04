package com.example.forcastingapp.database

import android.annotation.SuppressLint
import android.content.Context
import com.example.forcastingapp.model.SimpleWeatherData
import com.example.forcastingapp.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

class LocalRepository private constructor(context: Context) {
    private val favWeatherDao: favWeatherDao = database.getDatabase(context).favWeatherDao()
    private val weatherAlertDao: WeatherAlertDao = database.getDatabase(context).weatherAlertDao()

    // Methods for SimpleWeatherData
    suspend fun insertWeather(simpleWeather: SimpleWeatherData) {
        favWeatherDao.insertWeather(simpleWeather)
    }

    fun getAllWeather(): Flow<List<SimpleWeatherData>> {
        return favWeatherDao.getAllFav()
    }

    suspend fun deleteWeatherByCity(cityName: String) {
        favWeatherDao.deleteWeatherByCity(cityName)
    }

    // Methods for WeatherAlert
    suspend fun insertWeatherAlert(alert: WeatherAlert) {
        weatherAlertDao.insertAlert(alert)
    }

    fun getAllAlerts(): Flow<List<WeatherAlert>> {
        return weatherAlertDao.getAllAlerts()
    }

    suspend fun deleteAlertById(id: Int) {
        weatherAlertDao.deleteAlert(id)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: LocalRepository? = null

        fun getInstance(context: Context): LocalRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = LocalRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }
}
