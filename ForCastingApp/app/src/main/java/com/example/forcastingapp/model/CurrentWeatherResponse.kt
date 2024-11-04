package com.example.forcastingapp.model

data class CurrentWeatherResponse(
    val coord: Coord,
    val weather:  List<currWeatherList>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: currSys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)
data class currSys(
    val type:Int,
    val id:Int,
    val country:String,
    val sunrise:Long,
    val sunset:Long
)
data class currWeatherList( var id: Int,var main: String?, var description : String?, var icon: String?)