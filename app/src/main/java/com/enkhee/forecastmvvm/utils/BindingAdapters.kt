package com.enkhee.forecastmvvm.utils

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.enkhee.forecastmvvm.internal.glide.GlideApp
import com.enkhee.forecastmvvm.utils.extention.getParentActivity

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value -> view.visibility = value?:View.VISIBLE})
    }
}

@BindingAdapter("profileImage")
fun setProfileImage(view: ImageView, imageUrl:String){
    Log.v("setProfileImage", imageUrl)
    GlideApp.with(view.context)
        .load(imageUrl)
        .override(220, 220)
        .into(view)
}