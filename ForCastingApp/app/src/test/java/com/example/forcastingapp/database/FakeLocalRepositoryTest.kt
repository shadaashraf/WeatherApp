package com.example.forcastingapp.database

import com.example.forcastingapp.model.SimpleWeatherData
import com.example.forcastingapp.model.WeatherAlert
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test

class FakeLocalRepositoryTest {

    private lateinit var fakeLocalRepository: FakeLocalRepository

    @Before
    fun setup() {
        fakeLocalRepository = FakeLocalRepository()
    }

    @Test
    fun insertWeather_insertsWeatherCorrectly() = runTest {
        val weather = SimpleWeatherData(
            cityName = "Cairo",
            latitude = 30.0,
            longitude = 31.0,
            weatherIcon = 1,
            weatherDescription = "Sunny",
            temp = 25.0
        )
        fakeLocalRepository.insertWeather(weather)

        val allWeather = fakeLocalRepository.getAllWeather().first()

        assertThat(allWeather.first(), IsEqual(weather))
    }

    @Test
    fun deleteWeatherByCity_deletesWeatherCorrectly() = runTest {
        val weather = SimpleWeatherData(
            cityName = "Vienna",
            latitude = 48.2,
            longitude = 16.4,
            weatherIcon = 2,
            weatherDescription = "Cloudy",
            temp = 18.0
        )
        fakeLocalRepository.insertWeather(weather)
        fakeLocalRepository.deleteWeatherByCity("Vienna")

        val allWeather = fakeLocalRepository.getAllWeather().first()
        assertThat(allWeather.size, IsEqual(0))
    }

    @Test
    fun insertWeatherAlert_insertsAlertCorrectly() = runTest {
        val alert = WeatherAlert(1, 10,5, "Vienna")
        fakeLocalRepository.insertWeatherAlert(alert)

        val allAlerts = fakeLocalRepository.getAllAlerts().first()
        assertThat(allAlerts.first(), IsEqual(alert))
    }

    @Test
    fun deleteAlertById_deletesAlertCorrectly() = runTest {
        val alert = WeatherAlert(1, 10,5,"Vienna")

        fakeLocalRepository.insertWeatherAlert(alert)
        fakeLocalRepository.deleteAlertById(1)

        val allAlerts = fakeLocalRepository.getAllAlerts().first()
        assertThat(allAlerts.size, IsEqual(0))
    }
}
