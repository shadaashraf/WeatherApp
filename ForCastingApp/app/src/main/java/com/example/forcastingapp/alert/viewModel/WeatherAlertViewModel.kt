package com.example.forcastingapp.alert.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forcastingapp.alert.view.AlarmScheduler
import com.example.forcastingapp.database.WeatherRepository
import com.example.forcastingapp.model.Repository
import com.example.forcastingapp.model.WeatherAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WeatherAlertViewModel(
    private val repository: Repository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    // Flow for observing alerts
    val alerts: Flow<List<WeatherAlert>> = repository.getAllAlerts()

    fun setWeatherAlert(cityName: String, duration: Long, alarmType: Int) {
        // Save the alert in the database
        val weatherAlert = WeatherAlert(duration = duration, alarmType = alarmType, cityName = cityName)
        viewModelScope.launch {
            repository.insertWeatherAlert(weatherAlert)
            alarmScheduler.scheduleAlarm(duration, alarmType) // Schedule the alarm via AlarmScheduler
        }
    }

    fun cancelWeatherAlert(alertId: Int) {
        viewModelScope.launch {
            repository.deleteAlertById(alertId) // Remove the alert from the database
            alarmScheduler.cancelAlarm() // Cancel the alarm if needed
        }
    }
}
