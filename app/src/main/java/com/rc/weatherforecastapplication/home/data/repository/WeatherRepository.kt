package com.rc.weatherforecastapplication.home.data.repository

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.rc.weatherforecastapplication.database.WeatherDao
import com.rc.weatherforecastapplication.home.data.network.mapper.WeatherResponseMapper
import com.rc.weatherforecastapplication.home.data.network.service.WhetherService
import com.rc.weatherforecastapplication.home.presentation.viewmodel.WeatherInfo
import com.rc.weatherforecastapplication.home.presentation.viewmodel.mapper.WeatherInfoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherService: WhetherService,
    private val weatherDao: WeatherDao,
    private val weatherResponseMapper: WeatherResponseMapper,
    private val weatherInfoMapper: WeatherInfoMapper
) {

    suspend fun getWeatherConditionsOfLocation(city: String, location: LatLng): WeatherInfo {
        val lat = location.latitude
        val long = location.longitude
        val weather = weatherDao.getWeatherInfoOfLocation(city, lat, long)
        return if (weather == null || city.isNotEmpty()) {
            val weatherData =
                weatherResponseMapper.mapToEntityModel(weatherService.fetchWeatherReport(lat, long))
            Log.d("REPO", "Getting data from Server $weatherData")
            weatherDao.insert(weatherData)
            weatherInfoMapper.mapToViewModel(weatherData)
        } else {
            Log.d("REPO", "Getting data from DB")
            weatherInfoMapper.mapToViewModel(weather)
        }
    }

    fun getAllBookMarkedCity(): Flow<MutableList<WeatherInfo>> {
        return weatherDao.getBookMarkedLocations().map {
            it.map { weather ->
                weatherInfoMapper.mapToViewModel(weather)
            }.toMutableList()
        }
    }

    suspend fun deleteItem(city: String) = weatherDao.deleteItem(city) > 0
}