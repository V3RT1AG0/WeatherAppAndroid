package com.example.weatherapp.view.events

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.weatherapp.viewmodel.WeatherViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.loadImage
import com.example.weatherapp.model.Daily
import kotlinx.android.synthetic.main.activity_main.*
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import java.util.*
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.content.Intent
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.os.Looper
import android.transition.Explode
import android.transition.Slide
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import com.google.android.gms.location.*
import com.example.weatherapp.R
import com.example.weatherapp.model.Hourly
import com.example.weatherapp.view.search.SearchActivity


class WeatherMainActivity : AppCompatActivity() {


    lateinit var weatherViewModel: WeatherViewModel
    var PERMISSION_ID = 44
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val mylist = mutableListOf<Daily>()
    private val eventAdapter = WeatherAdapter(mylist)
    private val hourlyList = mutableListOf<Hourly>()
    private val hourlyAdapter = HourlyAdapter(hourlyList)
    private val SEARCH_ACTIVITY_ID = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.elevation = 0F
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }
        horizontal_recycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyAdapter
        }
        setObservers()
        getLastLocation()
    }


    private fun setObservers() {
        weatherViewModel.weatherDetails.observe(this, Observer { events ->
            events?.let {
                eventAdapter.refresh(it.daily.drop(1))
                hourlyAdapter.refresh(it.hourly)
                val today = it.today
                city.text = "${today.city}, ${today.state}"
                max.text = "${today.highTemp!!.toInt()} \u2109"
                min.text = "${today.lowTemp!!.toInt()} \u2109"
                current.text = "${today.currentTemp!!.toInt()} \u2109"
                description.text = today.description
                poster.loadImage(today.logo)
                recyclerView.visibility = View.VISIBLE
                Top.visibility = View.VISIBLE
            }
        })

        weatherViewModel.loading.observe(this, Observer { loading ->
            loading?.let {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    recyclerView.visibility = View.GONE
                    Top.visibility = View.GONE
                    error_view.visibility = View.GONE
                }
            }
        })

        weatherViewModel.error.observe(this, Observer { errorOccoured ->
            errorOccoured?.let {
                error_view.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    recyclerView.visibility = View.GONE
                    Top.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }
        })
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item -> {
                val i = Intent(this, SearchActivity::class.java)
                startActivityForResult(i, SEARCH_ACTIVITY_ID)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SEARCH_ACTIVITY_ID ->
                if (resultCode == Activity.RESULT_OK) {
                    val zip = data!!.getStringExtra ("zip")
                    if (zip == "0") {
                        getLastLocation()
                    } else
                        weatherViewModel.fetchDatafor(zip)
                }
        }
    }


        private fun checkPermissions(): Boolean {
            return ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }

        private fun requestPermissions() {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_ID
            )
        }

        private fun isLocationEnabled(): Boolean {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == PERMISSION_ID) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation()
                }
            }
        }


        @SuppressLint("MissingPermission")
        private fun getLastLocation() {
            Log.d("reached", "logeger")
            if (checkPermissions()) {
                Log.d("reached", "logeger")
                if (isLocationEnabled()) {
                    Log.d("reached", "logeger")
                    mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                        val location = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            Log.d("reached", "logeger")
                            updateWeatherWithLocation(location)
                        }
                    }
                } else {
                    Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                    val intent = Intent(ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            } else {
                requestPermissions()
            }
        }

        @SuppressLint("MissingPermission")
        private fun requestNewLocationData() {

            val locationRequest = LocationRequest.create()?.apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                numUpdates = 1
            }

            var mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            mFusedLocationClient.requestLocationUpdates(
                locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locatonResult: LocationResult?) {
                        super.onLocationResult(locatonResult)
                        if (locatonResult != null) {
                            updateWeatherWithLocation(locatonResult.lastLocation)
                        }
                    }
                },
                Looper.myLooper()
            )

        }

        private fun updateWeatherWithLocation(location: Location) {
            val geocoder = Geocoder(this, Locale.getDefault())
            val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)[0]
            address.let {
                val postalCode = it.postalCode
                weatherViewModel.fetchDatafor(postalCode)
            }

        }


    }
