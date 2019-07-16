package com.enkhee.forecastmvvm.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.enkhee.forecastmvvm.data.db.entity.WeatherLocation
import com.enkhee.forecastmvvm.internal.LocationPermissionNotGrantedException
import com.enkhee.forecastmvvm.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import kotlin.math.abs

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient : FusedLocationProviderClient,
    context : Context
) : PreferencesProvider(context), LocationProvider {
    private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(lastWeatherLocation : WeatherLocation) : Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e : LocationPermissionNotGrantedException) {
            false
        }

        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation)
    }

    private fun hasCustomLocationChanged(lastWeatherLocation : WeatherLocation) : Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            return customLocationName != lastWeatherLocation.name
        }
        return false
    }

    private fun getCustomLocationName() : String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation : WeatherLocation) : Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await() ?: return false

        val comparisonThreshold = 0.03
        return abs(deviceLocation.latitude - lastWeatherLocation.lat) > comparisonThreshold &&
                abs(deviceLocation.longitude - lastWeatherLocation.lon) > comparisonThreshold
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation() : Deferred<Location?> {
        return if (hasLocationPermission()) {
            Log.v("LastDeviceLocation", "Has Permission")
            fusedLocationProviderClient.lastLocation.asDeferred()
        } else {
            Log.v("LastDeviceLocation", "Failed Exception")
            throw LocationPermissionNotGrantedException()
        }
    }

    private fun hasLocationPermission() : Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isUsingDeviceLocation() : Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    override suspend fun getPreferredLocationString() : String {
        Log.v("LocationProviderImpl", "start")
        if (isUsingDeviceLocation()) {

            Log.v("LocationProviderImpl", "Device Location: ${isUsingDeviceLocation()}")
            try {
                Log.v("LocationProviderImpl", "before deviceLocation")
                val deviceLocation = getLastDeviceLocation().await()

                if (deviceLocation == null) {
                    Log.v("LocationProviderImpl", "deviceLocation null")
                    return "${getCustomLocationName()}"
                }

                Log.v("LocationProviderImpl", "success: ${deviceLocation.latitude}, ${deviceLocation.longitude}")
                return "${deviceLocation.latitude}, ${deviceLocation.longitude}"
            } catch (e : LocationPermissionNotGrantedException) {
                Log.v("LocationProviderImpl", "failed")
                return "${getCustomLocationName()}"
            }
        } else {

            Log.v("LocationProviderImpl", "success: ${isUsingDeviceLocation()}")
            return "${getCustomLocationName()}"
        }
    }
}