package com.example.forcastingapp.alert.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.forcastingapp.alert.viewModel.WeatherAlertViewModel
import com.example.forcastingapp.alert.viewModel.WeatherAlertViewModelFactory
import com.example.forcastingapp.database.LocalRepository
import com.example.forcastingapp.model.Repository
import com.example.forcastingapp.network.WeatherRemoteDataSourceImpl
import com.example.weatherforecast.R
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AlertFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAlertAdapter: WeatherAlertAdapter
    private val viewModel: WeatherAlertViewModel by viewModels {
        val alarmScheduler = AlarmScheduler(requireContext())
        WeatherAlertViewModelFactory(
            Repository.getInstance(LocalRepository.getInstance(requireContext()), WeatherRemoteDataSourceImpl.getInstance()),
            alarmScheduler
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_alerts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the WeatherAlertAdapter
        weatherAlertAdapter = WeatherAlertAdapter { alertId ->
            viewModel.cancelWeatherAlert(alertId)
        }
        recyclerView.adapter = weatherAlertAdapter

        // Observe alerts from the ViewModel
        viewModel.alerts.onEach { alerts ->
            weatherAlertAdapter.submitList(alerts) // Update alerts in the adapter
        }.launchIn(lifecycleScope)

        // Set up Spinner and Buttons
        setupUi(view)
    }

    private fun setupUi(view: View) {
        val spinner: Spinner = view.findViewById(R.id.spinner_alarm_type)
        // ArrayAdapter setup...

        view.findViewById<Button>(R.id.button_add_alert).setOnClickListener {
            val durationText = view.findViewById<EditText>(R.id.editText_duration).text.toString()
            if (durationText.isNotEmpty()) {
                val duration = durationText.toLong()
                val alarmType = spinner.selectedItemPosition
                viewModel.setWeatherAlert("City Name", duration, alarmType) // Adjust as necessary
            }
        }

        view.findViewById<Button>(R.id.button_stop_alert).setOnClickListener {
            // Functionality to stop alerts can be implemented here
        }
    }
}
