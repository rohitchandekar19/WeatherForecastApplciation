package com.rc.weatherforecastapplication.home.data.network.api

import com.rc.weatherforecastapplication.home.data.network.responsemodel.WeatherResponseEntity
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherApi {

    @GET("/data/2.5/weather")
    suspend fun fetchWeatherReport(@QueryMap queryParam: HashMap<String, String>): WeatherResponseEntity

}