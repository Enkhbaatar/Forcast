package com.enkhee.forecastmvvm.data.provider

import com.enkhee.forecastmvvm.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}