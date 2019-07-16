package com.enkhee.forecastmvvm.data.provider

import com.enkhee.forecastmvvm.data.db.entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation : WeatherLocation): Boolean
    suspend fun getPreferredLocationString():String
}