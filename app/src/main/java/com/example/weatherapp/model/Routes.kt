package com.example.weatherapp.model

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

data class ZipCode(val zipcode: String)

interface Routes {
    @POST("/api/ctl/weather")
    fun getWeatherfor(@Body zipcode:ZipCode): Single<Weather>
}
