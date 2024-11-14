package com.example.forcastingapp.database
import com.example.forcastingapp.model.SimpleWeatherData
import com.example.forcastingapp.model.WeatherAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalRepository(
    private val weatherDataList: MutableList<SimpleWeatherData> = mutableListOf(),
    private val alertList: MutableList<WeatherAlert> = mutableListOf()
) : ILocalRepository {

    // Methods for SimpleWeatherData
    override suspend fun insertWeather(simpleWeather: SimpleWeatherData) {
        weatherDataList.add(simpleWeather)
    }

    override fun getAllWeather(): Flow<List<SimpleWeatherData>> {
        return flow { emit(weatherDataList) }
    }

    override suspend fun deleteWeatherByCity(cityName: String) {
        weatherDataList.removeAll { it.cityName == cityName }
    }

    // Methods for WeatherAlert
    override suspend fun insertWeatherAlert(alert: WeatherAlert) {
        alertList.add(alert)
    }

    override fun getAllAlerts(): Flow<List<WeatherAlert>> {
        return flow { emit(alertList) }
    }

    override suspend fun deleteAlertById(id: Int) {
        alertList.removeAll { it.id == id }
    }
}
