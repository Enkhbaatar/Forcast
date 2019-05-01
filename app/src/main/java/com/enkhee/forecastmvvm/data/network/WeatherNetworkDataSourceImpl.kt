package com.enkhee.forecastmvvm.data.network

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.enkhee.forecastmvvm.data.network.response.CurrentWeatherResponse
import com.enkhee.forecastmvvm.internal.NoConnectivityException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WeatherNetworkDataSourceImpl(
    private val apixuWeatherApiService: ApixuWeatherApiService
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    @SuppressLint("CheckResult")
    override fun fetchCurrentWeather(location: String, languageCode: String) {
        try{
            Log.v("fetchCurrentWeather", "working")
            apixuWeatherApiService.getCurrentWeather(location, languageCode).subscribeOn(
                Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onStart() }
                .doOnTerminate { onFinish() }
                .subscribe(
                    { result -> onSuccess(result) },
                    { error -> onError(error) }
                )
        } catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection.", e)
        }
    }

    private fun onStart(){
        Log.v("getCurrentWeather", "Start")
    }

    private fun onFinish() {
        Log.v("getCurrentWeather", "Start")
    }

    private fun onError(error:Throwable){
        Log.v("getCurrentWeather", "Error")
    }

    private fun onSuccess(result:CurrentWeatherResponse){
        Log.v("getCurrentWeather", "Success")
        _downloadedCurrentWeather.postValue(result)
    }
}