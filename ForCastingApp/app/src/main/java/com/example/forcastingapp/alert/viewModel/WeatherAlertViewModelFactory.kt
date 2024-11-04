package com.example.forcastingapp.alert.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forcastingapp.database.WeatherRepository
import com.example.forcastingapp.alert.view.AlarmScheduler
import com.example.forcastingapp.model.Repository

class WeatherAlertViewModelFactory(
    private val repository: Repository,
    private val alarmScheduler: AlarmScheduler
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherAlertViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherAlertViewModel(repository, alarmScheduler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
