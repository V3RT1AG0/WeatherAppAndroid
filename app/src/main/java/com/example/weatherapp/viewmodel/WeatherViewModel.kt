package com.example.weatherapp.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()
    val disposable = CompositeDisposable()
    val weatherDetails = MutableLiveData<Weather>()

    init {
        loading.value = true
        error.value = false
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun fetchDatafor(zip: String) {
        loading.value = true
        fetchWeatherfor(zip)
    }


    private fun fetchWeatherfor(zip: String) {
        disposable.add(
            ApiService.getWeatherDetails().getWeatherfor(ZipCode(zip))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Weather>() {
                    override fun onSuccess(data: Weather) {
                        Log.d("logger", data.toString())
                        if (data.today==null) {
                            loading.value = false
                            error.value = true
                        }
                        else
                        {
                            loading.value = false
                            error.value = false
                            weatherDetails.value = data
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.e("error", e.message)
                        loading.value = false
                        error.value = true
                    }
                })
        )
    }
}
