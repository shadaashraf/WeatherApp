package com.example.forcastingapp.fav.view

import MapFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forcastingapp.database.LocalRepository
import com.example.forcastingapp.fav.viewModel.FavoritesViewModel
import com.example.forcastingapp.fav.viewModel.FavoritesViewModelFactory
import com.example.forcastingapp.home.view.WeatherFragment
import com.example.forcastingapp.home.view.onClicklistener
import com.example.forcastingapp.model.Repository
import com.example.forcastingapp.network.WeatherRemoteDataSourceImpl
import com.example.weatherforecast.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavoritesFragment : Fragment() ,onClicklistener{

    // Assuming you have a way to obtain the Repository instance
    private val repository: Repository by lazy {
        Repository.getInstance(
            LocalRepository.getInstance(requireContext()),
            WeatherRemoteDataSourceImpl.getInstance()
        )
    }

    // Initialize the ViewModel with the factory
    private val favoritesViewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(repository)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fav, container, false)

        recyclerView = view.findViewById(R.id.recyclerView_favorites)
        val fabAddCity: FloatingActionButton = view.findViewById(R.id.fab_add_city)

        // Set up RecyclerView
        favoritesAdapter = FavoritesAdapter(emptyList(), object : OnCityClickListener {
            override fun onCityClick(latitude: Double, longitude: Double) {
                val weatherFragment = WeatherFragment()

                // Create a Bundle to hold the latitude and longitude
                val args = Bundle().apply {
                    putDouble("latitude", latitude)
                    putDouble("longitude", longitude)
                }

                // Set the arguments to the fragment
                weatherFragment.arguments = args

                // Begin the transaction to replace the current fragment with WeatherFragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_content, weatherFragment)
                    .addToBackStack(null) // Optional: Add to back stack to allow navigating back
                    .commit()
            }

            override fun delete(cityName: String) {
                favoritesViewModel.removeFavoriteCity(cityName)
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = favoritesAdapter

        // Observe favorite cities
        favoritesViewModel.favoriteCities.observe(viewLifecycleOwner) { cities ->
            favoritesAdapter.updateCities(cities)
        }

        // Navigate to add city fragment
        fabAddCity.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_content,MapFragment())
                .addToBackStack(null) // Optional: Add to back stack to allow navigating back
                .commit()
        }

        return view
    }
}
