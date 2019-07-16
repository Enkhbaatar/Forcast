package com.enkhee.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.enkhee.forecastmvvm.R
import com.enkhee.forecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry
import com.enkhee.forecastmvvm.databinding.CurrentWeatherFragmentBinding
import com.enkhee.forecastmvvm.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware, CurrentWeatherCallBack {
    val LOG_TAG = "CurrentWeatherFragment"
    private lateinit var binding: CurrentWeatherFragmentBinding
    override val kodein by kodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private lateinit var viewModel: CurrentWeatherViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.current_weather_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.callBack = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindUI()

        val weatherObserver = Observer<UnitSpecificCurrentWeatherEntry> {
            Log.v(LOG_TAG, "weatherObserver: " + it.toString())
            //tv_temperature.text = it.temperature.toString()
        }
        viewModel.currentWeather.observe(this, weatherObserver)
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.location.await()

        weatherLocation.observe(this@CurrentWeatherFragment, Observer {
            if(it == null) return@Observer
            viewModel.weatherLocation.value = it
            updateLocation(it.name)
        })

        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer
            viewModel.loadingVisibility.value = View.GONE
            viewModel.currentWeather.value = it
            updateDateToToday()
        })
    }

    override fun onClick(view: View){

    }

    private fun updateLocation(location:String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }
}
