package com.example.forcastingapp.model


import com.example.forcastingapp.database.FakeLocalRepository
import com.example.forcastingapp.FakeRemoteData
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Test
import kotlinx.coroutines.flow.first
class RepositoryTest {

    // Sample Weather Data
    val location1= SimpleWeatherData(
        cityName = "Cairo",
        latitude = 30.0,
        longitude = 31.0,
        weatherIcon = 1,
        weatherDescription = "Sunny",
        temp = 25.0
    );
    val location2 = SimpleWeatherData(1,"Roma", 32.0, 33.0, 2,"CLoudy",34.4)

    // Mock data for local and remote repositories
    val localWeatherData = mutableListOf(location1)
    val fakeLocalDataSource = FakeLocalRepository(localWeatherData)
    val fakeRemoteDataSource = FakeRemoteData()

    val repo = Repository.getInstance(fakeLocalDataSource, fakeRemoteDataSource)

    @Test
    fun insertWeather_weatherData_added() = runTest {
        // Given a new location
        val newLocation = SimpleWeatherData(2, "New York", 25.0, 27.0, 3, "hot", 21.3)

        // When inserting weather data
        repo.insertWeather(newLocation)

        // Then the new location should be added to the local repository
        val allWeather = repo.getAllWeather().first() // Collect the first value emitted by the flow
        assertThat(allWeather.size == 1, IsEqual(true)) // Check that the size is 2
        assertThat(allWeather.contains(newLocation), IsEqual(true)) // Verify the new location is in the list
    }

    @Test
    fun deleteWeather_weatherData_removed() = runTest {
        // When deleting a weather location
        repo.deleteWeatherByCity("Cairo")

        // Then the location should be removed from the local repository
        val allWeather = repo.getAllWeather().first() // Collect the first value emitted by the flow
        assertThat(allWeather.size == 0, IsEqual(true)) // Expecting no weather data after deletion
    }
}
