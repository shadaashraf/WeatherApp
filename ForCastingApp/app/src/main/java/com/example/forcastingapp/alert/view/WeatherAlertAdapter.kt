package com.example.forcastingapp.alert.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.forcastingapp.model.WeatherAlert
import com.example.weatherforecast.R

class WeatherAlertAdapter(
    private val onDeleteClick: (Int) -> Unit
) : ListAdapter<WeatherAlert, WeatherAlertAdapter.AlertViewHolder>(AlertDiffCallback()) {

    inner class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val durationTextView: TextView = itemView.findViewById(R.id.text_view_duration)
        private val alarmTypeTextView: TextView = itemView.findViewById(R.id.text_view_alarm_type)
        private val deleteButton: TextView = itemView.findViewById(R.id.button_delete)

        fun bind(alert: WeatherAlert) {
            durationTextView.text = "Duration: ${alert.duration} ms"
            alarmTypeTextView.text = "Type: ${alert.alarmType}" // Modify as needed
            deleteButton.setOnClickListener { onDeleteClick(alert.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather_alert, parent, false)
        return AlertViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.bind(getItem(position)) // Use getItem to get the current item
    }

    override fun getItemCount(): Int {
        return super.getItemCount() // This will return the current list size
    }

    // DiffUtil Callback
    class AlertDiffCallback : DiffUtil.ItemCallback<WeatherAlert>() {
        override fun areItemsTheSame(oldItem: WeatherAlert, newItem: WeatherAlert): Boolean {
            return oldItem.id == newItem.id // Assuming id is a unique identifier
        }

        override fun areContentsTheSame(oldItem: WeatherAlert, newItem: WeatherAlert): Boolean {
            return oldItem == newItem // Check for equality of contents
        }
    }
}
