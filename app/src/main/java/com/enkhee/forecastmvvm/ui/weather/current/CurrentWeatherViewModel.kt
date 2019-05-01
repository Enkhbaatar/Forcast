package com.enkhee.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel;
import com.enkhee.forecastmvvm.data.repository.ForecastRepository
import com.enkhee.forecastmvvm.internal.UnitSystem
import com.enkhee.forecastmvvm.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository:ForecastRepository
) : ViewModel() {
    private val unitSystem = UnitSystem.METIC
    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }
}
