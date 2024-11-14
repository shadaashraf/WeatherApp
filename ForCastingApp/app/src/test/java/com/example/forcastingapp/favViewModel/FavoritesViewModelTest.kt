package com.example.forcastingapp.fav.viewModel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.forcastingapp.OurNewRule
import com.example.forcastingapp.database.FakeRepository
import com.example.forcastingapp.model.IRepository
import com.example.forcastingapp.model.SimpleWeatherData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesViewModelTest {

    lateinit var favoritesViewModel: FavoritesViewModel
    lateinit var repository: IRepository

    val weather1 = SimpleWeatherData(
        cityName = "Cairo",
        latitude = 30.0,
        longitude = 31.0,
        weatherIcon = 1,
        weatherDescription = "Sunny",
        temp = 25.0
    )

    val weather2 = SimpleWeatherData(
        cityName = "Vienna",
        latitude = 48.2,
        longitude = 16.4,
        weatherIcon = 2,
        weatherDescription = "Cloudy",
        temp = 18.0
    )

    @get:Rule
    val ourNewRule = OurNewRule()

    @Before
    fun setUp() {
        repository = FakeRepository()  // Your Fake Repository
        favoritesViewModel = FavoritesViewModel(repository)
    }

    @Test
    fun insertWeather_insertsWeatherCorrectly() = runTest {
        // Insert weather data
        repository.insertWeather(weather1)

        // Advance until idle to ensure coroutines are finished
        advanceUntilIdle()

        // Directly access the value of the StateFlow
        val favoriteCities = favoritesViewModel.favoriteCities.value

        // Verify the data is in the list
        assertThat(favoriteCities, `is`(listOf(weather1)))
    }

    @Test
    fun removeFavoriteCity_removesCityCorrectly() = runTest {
        // Insert two cities
        repository.insertWeather(weather1)
        repository.insertWeather(weather2)

        // Advance until idle to ensure coroutines are finished
        advanceUntilIdle()

        // Directly access the value of the StateFlow
        val favoriteCitiesBefore = favoritesViewModel.favoriteCities.value

        // Verify both cities are in the list
        if (favoriteCitiesBefore != null) {
            assertThat(favoriteCitiesBefore.size, `is`(2))
        }

        // Remove the second city (Vienna)
        favoritesViewModel.removeFavoriteCity(weather2.cityName)

        // Advance until idle again to ensure the data is updated
        advanceUntilIdle()

        // Collect the updated list
        val favoriteCitiesAfter = favoritesViewModel.favoriteCities.value

        // Verify that only the first city is left
        if (favoriteCitiesAfter != null) {
            assertThat(favoriteCitiesAfter.size, `is`(1))
        }
        assertThat(favoriteCitiesAfter?.firstOrNull()?.cityName, `is`(weather1.cityName))
        // Use the `cityName` property for comparison
    }
}
