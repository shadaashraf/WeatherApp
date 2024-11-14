package com.example.forcastingapp.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.forcastingapp.model.Weather
import com.example.forcastingapp.model.WeatherList
import com.example.weatherforecast.R

class FutureWeatherAdapter : ListAdapter<WeatherList, FutureWeatherAdapter.FutureWeatherViewHolder>(WeatherDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FutureWeatherViewHolder {
        // Inflate the layout directly
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_weather, parent, false)
        return FutureWeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: FutureWeatherViewHolder, position: Int) {
        val weatherItem = getItem(position)
        holder.bind(weatherItem)
    }

    class FutureWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val forecastDate: TextView = itemView.findViewById(R.id.day) // Replace with your TextView ID
        private val forecastTemp: TextView = itemView.findViewById(R.id.temp) // Replace with your TextView ID
        private val forecastIcon: ImageView = itemView.findViewById(R.id.icon) // Replace with your ImageView ID

        fun bind(weatherItem: WeatherList) {
            forecastDate.text = weatherItem.dt_txt // Assuming dt_txt is a String
            forecastTemp.text = "${weatherItem.main!!.temp_max} K/${weatherItem.main.temp_min} K" // Set temperature

            // Load the weather icon
//            val iconUrl = "https://openweathermap.org/img/wn/${weatherItem.weather!![0].icon}@2x.png"
//            Glide.with(itemView.context).load(iconUrl).into(forecastIcon)
        }
    }

    class WeatherDiffCallback : DiffUtil.ItemCallback<WeatherList>() {
        override fun areItemsTheSame(oldItem: WeatherList, newItem: WeatherList): Boolean {
            return oldItem.dt_txt == newItem.dt_txt // Assuming dt_txt is unique for each weather item
        }

        override fun areContentsTheSame(oldItem: WeatherList, newItem: WeatherList): Boolean {
            return oldItem == newItem
        }
    }
}
