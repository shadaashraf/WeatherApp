package com.example.forcastingapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.forcastingapp.model.SimpleWeatherData
import com.example.forcastingapp.model.WeatherAlert

@Database(entities = [SimpleWeatherData::class, WeatherAlert::class], version = 1)
abstract class database : RoomDatabase() {

    abstract fun favWeatherDao(): favWeatherDao
    abstract fun weatherAlertDao(): WeatherAlertDao

    companion object {
        @Volatile
        private var INSTANCE: database? = null

        fun getDatabase(context: Context): database {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    database::class.java,
                    "simple_weather_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
