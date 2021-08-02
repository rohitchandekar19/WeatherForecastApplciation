package com.rc.weatherforecastapplication.home.presentation.viewmodel

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.rc.weatherforecastapplication.home.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    private var weatherMutableLiveData = MutableLiveData<WeatherInfo>()
    val weatherLiveData: LiveData<WeatherInfo> get() = weatherMutableLiveData

    val allCityWeatherLiveData: LiveData<MutableList<WeatherInfo>>
        get() = weatherRepository.getAllBookMarkedCity().asLiveData()

    fun getWeatherConditionsOfLocation(city: String, location: LatLng) {
        viewModelScope.launch {
            val weather = weatherRepository.getWeatherConditionsOfLocation(city, location)
            withContext(Dispatchers.Main) {
                weatherMutableLiveData.value = weather
            }
        }
    }

    suspend fun deleteItem(city: String) = weatherRepository.deleteItem(city)
    fun resetLiveData() {
        weatherMutableLiveData = MutableLiveData<WeatherInfo>()
    }
}