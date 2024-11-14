package com.example.forcastingapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.forcastingapp.model.SimpleWeatherData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class FavWeatherDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: database
    private lateinit var favWeatherDao: favWeatherDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            database::class.java
        ).allowMainThreadQueries()
            .build()
        favWeatherDao = db.favWeatherDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertWeather_retrievesInsertedData() = runBlockingTest {
        val weatherData = SimpleWeatherData(
            id = 1,
            cityName = "Test City",
            latitude = 35.0,
            longitude = -120.0,
            weatherIcon = 800,
            weatherDescription = "Clear sky",
            temp = 25.0
        )

        favWeatherDao.insertWeather(weatherData)

        val result = favWeatherDao.getAllFav().first()

        assertThat(result, notNullValue())
        assertThat(result.size, `is`(1))
        assertThat(result[0].cityName, `is`("Test City"))
        assertThat(result[0].latitude, `is`(35.0))
        assertThat(result[0].longitude, `is`(-120.0))
        assertThat(result[0].weatherIcon, `is`(800))
        assertThat(result[0].weatherDescription, `is`("Clear sky"))
        assertThat(result[0].temp, `is`(25.0))
    }

    @Test
    fun insertWeather_overwritesDataWithSameCityName() = runBlockingTest {
        val initialWeatherData = SimpleWeatherData(
            id = 1,
            cityName = "Test City",
            latitude = 35.0,
            longitude = -120.0,
            weatherIcon = 800,
            weatherDescription = "Clear sky",
            temp = 20.0
        )
        val updatedWeatherData = SimpleWeatherData(
            id = 1,
            cityName = "Test City",  // Same city name for unique constraint
            latitude = 36.0,
            longitude = -121.0,
            weatherIcon = 801,
            weatherDescription = "Few clouds",
            temp = 30.0
        )

        favWeatherDao.insertWeather(initialWeatherData)
        favWeatherDao.insertWeather(updatedWeatherData)

        val result = favWeatherDao.getAllFav().first()

        assertThat(result.size, `is`(1))
        assertThat(result[0].cityName, `is`("Test City"))
        assertThat(result[0].latitude, `is`(36.0))
        assertThat(result[0].longitude, `is`(-121.0))
        assertThat(result[0].weatherIcon, `is`(801))
        assertThat(result[0].weatherDescription, `is`("Few clouds"))
        assertThat(result[0].temp, `is`(30.0))
    }
}
