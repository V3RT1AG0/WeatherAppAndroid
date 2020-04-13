package com.example.weatherapp.model


import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object ApiService{

    private val BASE_URL = "https://j9l4zglte4.execute-api.us-east-1.amazonaws.com"

    fun getWeatherDetails(): Routes {
        val weather_routes = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(Routes::class.java)
        return weather_routes
    }
}