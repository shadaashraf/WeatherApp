package com.example.forcastingapp.fav.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forcastingapp.model.IRepository
import com.example.forcastingapp.model.SimpleWeatherData
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: IRepository) : ViewModel() {

    private val _favoriteCities = MutableLiveData<List<SimpleWeatherData?>>()
    val favoriteCities: LiveData<List<SimpleWeatherData?>> get() = _favoriteCities

    init {
        fetchFavoriteCities()
    }

    // Collects the flow of favorite cities and updates _favoriteCities
    private fun fetchFavoriteCities() {
        viewModelScope.launch {
            try {
                repository.getAllWeather().collect { data ->
                    _favoriteCities.value = data
                }
            } catch (e: Exception) {
                // Handle exceptions if necessary
            }
        }
    }

    // Deletes a city from favorites and refreshes the list
    fun removeFavoriteCity(cityName: String) {
        viewModelScope.launch {
            repository.deleteWeatherByCity(cityName)
            fetchFavoriteCities() // Refresh the list after deletion
        }
    }
}
