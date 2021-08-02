package com.rc.weatherforecastapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM Weather where Weather.city = :city or (Weather.lat = :latitude and Weather.longitude = :longitude)")
    suspend fun getWeatherInfoOfLocation(city: String, latitude: Double, longitude: Double): Weather

    @Query("SELECT * FROM Weather")
    fun getBookMarkedLocations(): Flow<MutableList<Weather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: Weather)

    @Query("DELETE FROM Weather where Weather.city = :city")
    suspend fun deleteItem(city: String): Int
}