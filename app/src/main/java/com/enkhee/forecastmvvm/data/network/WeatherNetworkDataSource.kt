package com.enkhee.forecastmvvm.data.network

import androidx.lifecycle.LiveData
import com.enkhee.forecastmvvm.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )
}