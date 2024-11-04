package com.example.forcastingapp.fav.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.example.forcastingapp.R
import com.example.forcastingapp.model.SimpleWeatherData

interface OnCityClickListener {
    fun onCityClick(latitude: Double, longitude: Double)
    fun delete(cityName: String)
}

class FavoritesAdapter(
    private var cities: List<SimpleWeatherData?>,
    private val listener: OnCityClickListener
) : RecyclerView.Adapter<FavoritesAdapter.CityViewHolder>() {

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.city_name)
        val temperature: TextView = itemView.findViewById(R.id.temperature)
        val weatherDescription: TextView = itemView.findViewById(R.id.weather_description)
        val deleteBtn:ImageButton=itemView.findViewById(R.id.delete)
        val cardLayout:CardView=itemView.findViewById(R.id.favCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favcard, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        if(city!=null) {
            holder.cityName.text = city.cityName
            holder.temperature.text = "${city.temp}°C"
            holder.weatherDescription.text = city.weatherDescription

            holder.itemView.setOnClickListener {
                listener.onCityClick(city.latitude,city.longitude)
            }
            holder.deleteBtn.setOnClickListener{
                listener.delete(city.cityName)
            }
        }
        else {
            Log.i("favAdapter", "onBindViewHolder: city is an emptyList ")
        }
    }

    override fun getItemCount(): Int = cities.size

    fun updateCities(newCities: List<SimpleWeatherData?>) {
        cities = newCities
        notifyDataSetChanged()
    }
}
