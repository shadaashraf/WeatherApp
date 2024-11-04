package com.example.forcastingapp.setting.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.forcastingapp.MainActivity
import com.example.forcastingapp.databinding.FragmentSettingBinding

import java.util.Locale

class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false )

        // Load saved language preference and check the appropriate radio button
        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        val savedLanguage = prefs.getString("language", "en")
        if (savedLanguage == "en") {
            binding.radioArabic.isChecked = false
            binding.radioEnglish.isChecked = true
        } else {
            binding.radioArabic.isChecked = true
            binding.radioEnglish.isChecked = false
        }
        val savedTemperature = prefs.getString("temperature", "c")
        if (savedTemperature == "c") {
            binding.radioCelsius.isChecked = true
            binding.radioKelvin.isChecked = false
            binding.radioFahrenheit.isChecked = false
        } else if (savedTemperature == "k") {
            binding.radioCelsius.isChecked = false
            binding.radioKelvin.isChecked = true
            binding.radioFahrenheit.isChecked = false
        } else {
            binding.radioCelsius.isChecked = false
            binding.radioKelvin.isChecked = false
            binding.radioFahrenheit.isChecked = true
        }
        val savedWindSpeed = prefs.getString("windSpeed", "meterPerSec")
        if (savedWindSpeed == "meterPerSec") {
            binding.radioMilesPerHour.isChecked = false
            binding.radioMeterPerSec.isChecked = true
        } else {
            binding.radioMilesPerHour.isChecked = true
            binding.radioMeterPerSec.isChecked = false
        }

        binding.radioEnglish.setOnClickListener {
            binding.radioArabic.isChecked = false
            binding.radioEnglish.isChecked = true
            setLanguage("en")
        }
        binding.radioArabic.setOnClickListener {
            binding.radioArabic.isChecked = true
            binding.radioEnglish.isChecked = false
            setLanguage("ar")
        }

        binding.radioCelsius.setOnClickListener {
            binding.radioCelsius.isChecked = true
            binding.radioKelvin.isChecked = false
            binding.radioFahrenheit.isChecked = false
            setTemperature("standard")
        }
        binding.radioKelvin.setOnClickListener {
            binding.radioCelsius.isChecked = false
            binding.radioKelvin.isChecked = true
            binding.radioFahrenheit.isChecked = false
            setTemperature("metric")
        }
        binding.radioFahrenheit.setOnClickListener {
            binding.radioCelsius.isChecked = false
            binding.radioKelvin.isChecked = false
            binding.radioFahrenheit.isChecked = true
            setTemperature("imperial")
        }

        binding.radioMeterPerSec.setOnClickListener {
            binding.radioMeterPerSec.isChecked = true
            binding.radioMilesPerHour.isChecked = false
            setWindSpeed("meterPerSec")
        }
        binding.radioMilesPerHour.setOnClickListener {
            binding.radioMeterPerSec.isChecked = false
            binding.radioMilesPerHour.isChecked = true
            setWindSpeed("milesPerHour")
        }



        return binding.root
    }

    private fun setLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Save language preference
        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("language", languageCode).apply()

        //activity?.recreate()
        (activity as? MainActivity)?.loadFragment(SettingFragment())
    }

    private fun setTemperature(temperature: String) {
        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("temperature", temperature).apply()

        (activity as? MainActivity)?.loadFragment(SettingFragment())

    }

    private fun setWindSpeed(windSpeed: String) {
        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("windSpeed", windSpeed).apply()

        (activity as? MainActivity)?.loadFragment(SettingFragment())
    }

    private fun setLocation(location: String) {
        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("location", location).apply()

        (activity as? MainActivity)?.loadFragment(SettingFragment())
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}