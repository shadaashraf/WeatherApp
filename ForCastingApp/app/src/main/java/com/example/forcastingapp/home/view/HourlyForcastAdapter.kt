package com.example.forcastingapp.home.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.forcastingapp.R
import com.example.forcastingapp.model.WeatherList

class HourlyForcastAdapter : ListAdapter<WeatherList, HourlyForcastAdapter.HourlyViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_card, parent, false)
        return HourlyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val weather = getItem(position)
        holder.bind(weather)
    }

    class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hourText: TextView = itemView.findViewById(R.id.hour)
        private val hourTempText: TextView = itemView.findViewById(R.id.hour_temp)



        fun bind(weather: WeatherList) {
            var hour= weather.dt_txt!!.subSequence(10,15)
            hourText.text = hour
            hourTempText.text = "${weather.main?.temp}°C"
            // Set weather icon (dummy drawable used here)

        }
    }

    class DiffCallback : DiffUtil.ItemCallback<WeatherList>() {
        override fun areItemsTheSame(oldItem: WeatherList, newItem: WeatherList) = oldItem.dt == newItem.dt
        override fun areContentsTheSame(oldItem: WeatherList, newItem: WeatherList) = oldItem == newItem
    }
}
