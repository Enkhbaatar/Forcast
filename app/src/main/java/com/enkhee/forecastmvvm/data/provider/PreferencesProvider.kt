package com.enkhee.forecastmvvm.data.provider

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

abstract class PreferencesProvider(context: Context) {
    private val appContext = context.applicationContext
    protected val preferences : SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)
}