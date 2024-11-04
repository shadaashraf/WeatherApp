package com.example.forcastingapp.home.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.forcastingapp.R
import com.example.forcastingapp.database.LocalRepository
import com.example.forcastingapp.home.viewModel.HomeViewModel
import com.example.forcastingapp.home.viewModel.HomeViewModelFactory
import com.example.forcastingapp.model.CurrentWeatherResponse
import com.example.forcastingapp.model.Repository
import com.example.forcastingapp.model.WeatherResponse
import com.example.forcastingapp.network.State
import com.example.forcastingapp.network.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import android.Manifest
import android.content.Context

class WeatherFragment : Fragment() {

    private lateinit var hourlyAdapter: HourlyForcastAdapter
    private lateinit var futureAdapter: FutureWeatherAdapter
    private lateinit var locationText: TextView
    private lateinit var dateText: TextView
    private lateinit var temperatureText: TextView
    private lateinit var weatherStatusText: TextView
    private lateinit var recyclerViewHourly: RecyclerView
    private lateinit var recyclerViewDay: RecyclerView
    private lateinit var tempIcon: ImageView
    private lateinit var pressureText: TextView
    private lateinit var humidityText: TextView
    private lateinit var windSpeedText: TextView
    private lateinit var sunsetText: TextView
    private lateinit var sunriseText: TextView
    private lateinit var  unitTemp: TextView
    private lateinit var locationProvider: LocationProvider
    private val repository: Repository by lazy {
        Repository.getInstance(
            LocalRepository.getInstance(requireContext()),
            WeatherRemoteDataSourceImpl.getInstance()
        )
    }
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requestLocationPermission()
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize LocationProvider
        locationProvider = LocationProvider(requireContext())

        // Initialize views
        locationText = view.findViewById(R.id.location)
        dateText = view.findViewById(R.id.date)
        temperatureText = view.findViewById(R.id.temperature)
        tempIcon = view.findViewById(R.id.weather_icon)
        weatherStatusText = view.findViewById(R.id.weather_status)
        pressureText = view.findViewById(R.id.pressure)
        humidityText = view.findViewById(R.id.humidity)
        windSpeedText = view.findViewById(R.id.wind_speed)
        sunsetText = view.findViewById(R.id.sunset)
        sunriseText = view.findViewById(R.id.sunrise)
        recyclerViewHourly = view.findViewById(R.id.recyclerView_hourly)
        recyclerViewDay = view.findViewById(R.id.recycleViewDay)
        unitTemp=view.findViewById(R.id.tempUnit)

        // Set up RecyclerView
        hourlyAdapter = HourlyForcastAdapter()
        recyclerViewHourly.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyAdapter
        }
        futureAdapter = FutureWeatherAdapter()
        recyclerViewDay.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = futureAdapter
        }

        // Check if arguments are passed for latitude and longitude
        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        if (latitude != null && longitude != null) {
            // If latitude and longitude are provided, use them
            Log.d("WeatherFragment", "Using provided latitude: $latitude, longitude: $longitude")
            updateUI(latitude, longitude)
        } else {
            Log.i("WeatherFragment", "onViewCreated: fetch")
            Log.i("WeatherFragment", "Permission granted: ${ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED}")

            fetchLocationAndWeather()
        }
    }

    private fun fetchLocationAndWeather() {
        CoroutineScope(Dispatchers.Main).launch {
            val location = locationProvider.getLastKnownLocation()
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                Log.d("WeatherFragment", "Latitude: $latitude, Longitude: $longitude")
                updateUI(latitude, longitude)
            } else {
                Log.e("WeatherFragment", "Last known location is null, requesting current location.")
                locationProvider.requestCurrentLocation { location ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude
                        Log.d("WeatherFragment", "Requested Location - Latitude: $latitude, Longitude: $longitude")
                        updateUI(latitude, longitude)
                    } ?: Log.e("WeatherFragment", "Failed to retrieve location.")
                }
            }
        }
    }


    private fun updateUI(latitude: Double, longitude: Double) {
        // Fetch weather data using ViewModel
        viewModel.fetchWeatherData(latitude, longitude, apiKey = "f8b536d2b58104f68af8c41bfd2cad06")
        viewModel.fetchCurrentWeatherData(latitude, longitude, apiKey = "f8b536d2b58104f68af8c41bfd2cad06")

        // Observing current weather data
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currWeatherData.collect { state ->
                when (state) {
                    is State.Loading -> {
                        Log.d("WeatherFragment", "Loading current weather data...")
                    }
                    is State.CurrWeatherSuccess -> {
                        state.currWeatherResponse?.let { updateCurrentWeatherUI(it, latitude, longitude) }
                    }
                    is State.Error -> {
                        Log.e("WeatherFragment", "Error: ${state.message}")
                    }
                    is State.WeatherSuccess -> {}
                }
            }
        }

        // Observing weather forecast data
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.weatherData.collect { state ->
                when (state) {
                    is State.Loading -> {
                        Log.d("WeatherFragment", "Loading weather forecast data...")
                    }
                    is State.WeatherSuccess -> {
                        state.weatherResponse?.let { updateWeatherForecastUI(it) }
                    }
                    is State.Error -> {
                        Log.e("WeatherFragment", "Error: ${state.message}")
                    }
                    is State.CurrWeatherSuccess -> {}
                }
            }
        }
    }

    private fun updateCurrentWeatherUI(
        currentWeather: CurrentWeatherResponse,
        latitude: Double,
        longitude: Double
    ) {
        val country = locationProvider.getLocationFromLatLng(latitude, longitude)
        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        val windSpeedUnit = prefs.getString("windSpeed", "meterPerSec")
        val unit = prefs.getString("temperature", "k")
        val tempText: String
        val unitText: String
        val tempInKelvin= currentWeather.main.temp
        val windSpeed = currentWeather.wind.speed
        when (unit) {
            "c" -> {
                // Convert Kelvin to Celsius
                val tempInCelsius = tempInKelvin - 273.15
                tempText = "%.1f".format(tempInCelsius)
                unitText = "°C"
            }
            "f" -> {
                // Convert Kelvin to Fahrenheit
                val tempInFahrenheit = (tempInKelvin - 273.15) * 9/5 + 32
                tempText = "%.1f".format(tempInFahrenheit)
                unitText = "°F"
            }
            else -> {
                // Default to Kelvin
                tempText = "%.1f".format(tempInKelvin)
                unitText = "K"
            }
        }

        locationText.text = "${currentWeather.name}, $country"
        dateText.text = convertUnixToCustomDateFormat(currentWeather.dt)
        temperatureText.text =tempText
        unitTemp.text=unitText
        weatherStatusText.text = currentWeather.weather[0].description
        pressureText.text = "${currentWeather.main.pressure} hPa"
        humidityText.text = "${currentWeather.main.humidity}%"
        val formattedWindSpeedText = when (windSpeedUnit) {
            "meterPerSec" -> "$windSpeed m/s"
            "milesPerHour" -> "${convertToMilesPerHour(windSpeed)} mph"
            else -> "$windSpeed m/s" // Fallback to meters per second
        }

// Set the text to display the wind speed in the chosen unit
        windSpeedText.text = formattedWindSpeedText
        sunsetText.text = convertUnixToCustomDateFormat(currentWeather.sys.sunset).substring(3)
        sunriseText.text = convertUnixToCustomDateFormat(currentWeather.sys.sunrise).substring(3)

        val iconUrl = "https://openweathermap.org/img/wn/${currentWeather.weather[0].icon}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(tempIcon)
    }

    private fun updateWeatherForecastUI(weatherResponse: WeatherResponse) {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        val todayWeatherList = weatherResponse.list.filter { item ->
            item.dt_txt?.startsWith(todayDate) == true
        }

        // Filter future weather to include only entries with the same time as `currentTime`
        val futureWeatherList = weatherResponse.list.filter { item ->
            !item.dt_txt!!.startsWith(todayDate) && item.dt_txt.contains(currentTime)
        }

        hourlyAdapter.submitList(todayWeatherList)
        futureAdapter.submitList(futureWeatherList)
    }

    private fun convertUnixToCustomDateFormat(unixTime: Long): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return dateFormat.format(Date(unixTime * 1000))
    }


    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            // Permission is already granted
            fetchLocationAndWeather()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch the location
                fetchLocationAndWeather()
            } else {
                // Permission denied
                Log.e("WeatherFragment", "Location permission denied.")
                // Optionally show a message to the user explaining why the permission is needed
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    private fun convertToMilesPerHour(metersPerSecond: Double): Double {
        return metersPerSecond * 2.237 // 1 m/s is approximately 2.237 mph
    }

}
