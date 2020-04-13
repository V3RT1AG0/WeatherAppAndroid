package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Today(
    @SerializedName("city")
    val city: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("iconLink")
    val logo: String,
    @SerializedName("highTemperature")
    val highTemp: Float,
    @SerializedName("lowTemperature")
    val lowTemp: Float,
    @SerializedName("temperature")
    val currentTemp: Float,
    @SerializedName("description")
    val description: String
    )

data class Daily(
    @SerializedName("weekday")
    val weekday: String?,
    @SerializedName("highTemperature")
    val highTemp: Float?,
    @SerializedName("lowTemperature")
    val lowTemp: Float?,
    @SerializedName("iconLink")
    val logo: String?
)

data class Hourly(
    @SerializedName("iconLink")
    val logo: String?,
    @SerializedName("temperature")
    val currentTemp: Float?,
    @SerializedName("localTime")
    val localTime: String?

)


data class Weather(
    @SerializedName("today")
    val today: Today,
    @SerializedName("daily")
    val daily: List<Daily>,
    @SerializedName("hourly")
    val hourly: List<Hourly>
)

