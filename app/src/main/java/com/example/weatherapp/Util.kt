package com.example.weatherapp

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


var sharedOptions = RequestOptions()
    .fitCenter()
    .error(R.drawable.ic_medal)

fun ImageView.loadImage(url:String?){
    Glide
        .with(this.context)
        .load(url)
        .apply(sharedOptions)
        .into(this)
}

fun getFormattedTime(hour:String): String {
    val hourOfDay  = hour.toInt()
    return when {
        hourOfDay<12 -> "$hourOfDay AM"
        hourOfDay==12 -> "$hourOfDay PM"
        else -> "${hourOfDay-12} PM"
    }
}

