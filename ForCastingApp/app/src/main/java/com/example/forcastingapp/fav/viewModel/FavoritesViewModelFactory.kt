package com.example.forcastingapp.fav.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forcastingapp.model.Repository

class FavoritesViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
