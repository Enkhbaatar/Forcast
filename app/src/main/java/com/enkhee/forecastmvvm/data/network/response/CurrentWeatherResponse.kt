package com.enkhee.forecastmvvm.data.network.response

import com.enkhee.forecastmvvm.data.db.entity.CurrentWeatherEntry
import com.enkhee.forecastmvvm.data.db.entity.Location
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    @SerializedName("location")
    val location: Location
)