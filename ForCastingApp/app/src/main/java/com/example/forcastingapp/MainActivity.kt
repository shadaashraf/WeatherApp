package com.example.forcastingapp

import MapFragment
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.forcastingapp.alert.view.AlertFragment
import com.example.forcastingapp.fav.view.FavoritesFragment
import com.example.forcastingapp.home.view.WeatherFragment
import com.example.forcastingapp.setting.view.SettingFragment
import com.example.weatherforecast.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load WeatherFragment as the initial fragment
        if (savedInstanceState == null) {
            loadFragment(WeatherFragment())
        }
        createNotificationChannel()
        setupNavigation()
    }

    private fun setupNavigation() {
        // Get references to each ImageButton
        val homeButton: ImageButton = findViewById(R.id.navigation_home)
        val searchButton: ImageButton = findViewById(R.id.navigation_search)
        val gridButton: ImageButton = findViewById(R.id.navigation_grid)
        val favButton:ImageButton=findViewById(R.id.navigation_save)
        // Set OnClickListeners for each button to load respective fragments
        homeButton.setOnClickListener {
            loadFragment(WeatherFragment())
        }

        searchButton.setOnClickListener {
            loadFragment(SettingFragment())
        }
        gridButton.setOnClickListener{
            loadFragment(MapFragment())
        }
        favButton.setOnClickListener{
            loadFragment(FavoritesFragment())
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "weather_alert_channel",
                "Weather Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for weather alerts"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    // Function to load a fragment into the main content area
    @SuppressLint("ResourceType")
    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, fragment)
            .commit()
    }
}
