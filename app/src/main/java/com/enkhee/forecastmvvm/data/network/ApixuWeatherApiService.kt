package com.enkhee.forecastmvvm.data.network

import com.enkhee.forecastmvvm.data.network.response.CurrentWeatherResponse
import com.enkhee.forecastmvvm.utils.API_KEY
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApixuWeatherApiService {

    @GET("current.json")
    fun getCurrentWeather(
        @Query("q") location:String,
        @Query("lang") language:String = "en"
    ):Observable<CurrentWeatherResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): ApixuWeatherApiService{
            val requestInterceptor = Interceptor{chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.apixu.com/v1/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApixuWeatherApiService::class.java)
        }
    }
}