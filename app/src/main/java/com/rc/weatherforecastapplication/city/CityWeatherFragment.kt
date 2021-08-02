package com.rc.weatherforecastapplication.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.rc.weatherforecastapplication.R
import com.rc.weatherforecastapplication.databinding.FragmentCityWeatherBinding
import com.rc.weatherforecastapplication.extension.getDisplayDateTime
import com.rc.weatherforecastapplication.home.presentation.viewmodel.WeatherInfo
import com.rc.weatherforecastapplication.home.presentation.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityWeatherFragment : Fragment() {

    private lateinit var binding: FragmentCityWeatherBinding
    private val viewModel: WeatherViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "City Weather Information"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeCItyWeather()
        arguments?.let {
            val cityWeather = it.getParcelable<WeatherInfo>(CITY_WEATHER)!!
            viewModel.getWeatherConditionsOfLocation(
                cityWeather.city,
                LatLng(cityWeather.lat, cityWeather.longitude)
            )
        }
    }

    private fun observeCItyWeather() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner, { cityWeather ->
            binding.cityTextView.text = cityWeather.city
            binding.descriptionTextView.text = cityWeather.description
            binding.temperatureTextView.text = getString(R.string.temp, cityWeather.temp)
            binding.tempMaxTextView.text = getString(R.string.temp_max, cityWeather.tempMax)
            binding.tempMinTextView.text = getString(R.string.temp_min, cityWeather.tempMin)
            binding.humidityTextView.text = getString(R.string.humid, cityWeather.humidity)
            binding.statusTextView.text = cityWeather.status
            binding.lastUpdatedTextView.text =
                getString(R.string.updated_at, cityWeather.lastUpdatedAt.getDisplayDateTime())
            Glide.with(this).load(STATUS_URI.plus(cityWeather.icon)).into(binding.statusImageView)
        })
    }

    companion object {
        private const val CITY_WEATHER = "city_weather"
        private const val STATUS_URI = "http://api.openweathermap.org/img/w/"
    }
}