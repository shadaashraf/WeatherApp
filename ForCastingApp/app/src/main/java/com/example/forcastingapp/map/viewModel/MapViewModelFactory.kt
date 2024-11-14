package com.example.forcastingapp.map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forcastingapp.database.ILocalRepository
import com.example.forcastingapp.model.Repository
import com.example.forcastingapp.network.WeatherRemoteDataSourceImpl

class MapViewModelFactory(
    private val localRepository: ILocalRepository,
    private val remoteRepository: WeatherRemoteDataSourceImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(Repository.getInstance(localRepository, remoteRepository)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
