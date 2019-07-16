package com.enkhee.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.enkhee.forecastmvvm.data.db.entity.WeatherLocation
import com.enkhee.forecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(metric:Boolean):LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation():LiveData<out WeatherLocation>
}