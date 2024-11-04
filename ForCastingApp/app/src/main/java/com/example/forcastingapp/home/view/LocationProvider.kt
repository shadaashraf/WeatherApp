package com.example.forcastingapp.home.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class LocationProvider(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    suspend fun getLastKnownLocation(): Location? {
        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.await()
        } else {
            null // Permission not granted, handle it in the fragment
        }
    }
    fun requestCurrentLocation(callback: (Location?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                callback(location) // Return the location through the callback
            }
        } else {
            callback(null) // If permission is not granted, return null
        }
    }
    fun getLocationFromLatLng(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context)
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                val address = addresses[0]
                val locality = address.locality ?: "Unknown locality"
                val country = address.countryName ?: "Unknown country"
                "$locality, $country"
            } else {
                "Location not found"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error retrieving location"
        }
    }
}
