package com.rc.weatherforecastapplication.home.data.network.service

import com.rc.weatherforecastapplication.home.data.network.api.WeatherApi
import com.rc.weatherforecastapplication.home.data.network.responsemodel.WeatherResponseEntity
import java.util.*
import javax.inject.Inject

class WhetherService @Inject constructor(private val weatherApi: WeatherApi) {

    suspend fun fetchWeatherReport(latitude: Double, longitude: Double): WeatherResponseEntity {
        return weatherApi.fetchWeatherReport(getQueryParam(latitude, longitude))
    }

    private fun getQueryParam(latitude: Double, longitude: Double): HashMap<String, String> {
        val queryMap = HashMap<String, String>()
        queryMap["lat"] = latitude.toString()
        queryMap["lon"] = longitude.toString()
        queryMap["appid"] = "fae7190d7e6433ec3a45285ffcf55c86"
        queryMap["units"] = "metric"
        return queryMap
    }
}