package com.example.adanapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.azan.Azan
import com.azan.Method
import com.azan.astrologicalCalc.Location
import com.azan.astrologicalCalc.SimpleDate
import com.example.adanap.AdanService
import com.google.android.gms.location.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var preferences : LocationPrefrence
    var adanService : Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        preferences = LocationPrefrence(this)
        getLastLocation()

        val  preferences = LocationPrefrence(this)

        setContentView(R.layout.activity_main)


        Handler().postDelayed({

            val latitude = preferences.getLat()
            val longitude = preferences.getLon()

                findViewById<TextView>(R.id.latTextView).text = latitude
                        findViewById<TextView>(R.id.lonTextView).text = longitude

            val today = SimpleDate(GregorianCalendar())
            val location = Location(latitude.toDouble(), longitude.toDouble(), 1.0, 0)
            val azan = Azan(location, Method.EGYPT_SURVEY)
            val prayerTimes = azan.getPrayerTimes(today)
            val imsaak = azan.getImsaak(today)

            println("date ---> " + today.day + " / " + today.month + " / " + today.year)

            findViewById<TextView>(R.id.imsaak_value).text = imsaak.toString()
            findViewById<TextView>(R.id.Fajr_value).text = prayerTimes.fajr().toString()
            findViewById<TextView>(R.id.sunrise_value).text = prayerTimes.shuruq().toString()
            findViewById<TextView>(R.id.Zuhr_value).text = prayerTimes.thuhr().toString()
            findViewById<TextView>(R.id.Asr_value).text = prayerTimes.assr().toString()
            findViewById<TextView>(R.id.Maghrib_value).text = prayerTimes.maghrib().toString()
            findViewById<TextView>(R.id.ISHA_value).text = prayerTimes.ishaa().toString()

            preferences.setprayers(imsaak.toString().take(4) , prayerTimes.fajr().toString().take(4) , prayerTimes.shuruq().toString().take(4)
                , prayerTimes.thuhr().toString().take(5) , prayerTimes.assr().toString().take(5) , prayerTimes.maghrib().toString().take(5)
                , prayerTimes.ishaa().toString().take(5))

            Handler().postDelayed({

            val adanService = Intent(this, AdanService::class.java)
            startService(adanService)

            } ,1000 )


        } ,500 )


    }

    override fun onDestroy() {
        stopService(adanService)
        super.onDestroy()
    }


    // Location section  ---------------------------------------------------------------------------------


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: android.location.Location? = task.result

                    if (location == null) {
                        requestNewLocationData()
                    } else { Log.e("Longitude", location.longitude.toString())
                        Log.e("Latitude", location.latitude.toString())
                        preferences.setLocation( location.latitude.toString() , location.longitude.toString()) }
                }

            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {

            var mLastLocation: android.location.Location = locationResult.lastLocation
            preferences.setLocation( mLastLocation.latitude.toString() ,  mLastLocation.longitude.toString())


        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }


}