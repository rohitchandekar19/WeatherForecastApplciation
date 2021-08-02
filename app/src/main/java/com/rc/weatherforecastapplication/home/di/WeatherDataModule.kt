package com.rc.weatherforecastapplication.home.di

import com.rc.weatherforecastapplication.home.data.network.api.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object WeatherDataModule {

    @Provides
    fun provideWeatherApi(retrofitBuilder: Retrofit.Builder): WeatherApi =
        retrofitBuilder.build().create(WeatherApi::class.java)
}