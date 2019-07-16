package com.enkhee.forecastmvvm.ui.weather.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enkhee.forecastmvvm.data.db.entity.WeatherLocation
import com.enkhee.forecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry
import com.enkhee.forecastmvvm.data.provider.UnitProvider
import com.enkhee.forecastmvvm.data.repository.ForecastRepository
import com.enkhee.forecastmvvm.internal.UnitSystem
import com.enkhee.forecastmvvm.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository : ForecastRepository,
    unitProvider : UnitProvider
) : ViewModel() {
    val loadingVisibility : MutableLiveData<Int> = MutableLiveData()
    val currentWeather : MutableLiveData<UnitSpecificCurrentWeatherEntry> = MutableLiveData()
    val weatherLocation : MutableLiveData<WeatherLocation> = MutableLiveData()

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric : Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }

    val location by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}