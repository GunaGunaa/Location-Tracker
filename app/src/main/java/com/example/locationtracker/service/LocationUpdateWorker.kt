package com.example.locationtracker.service

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.locationtracker.LTApplicationClass
import com.example.locationtracker.model.LocationData
import com.example.locationtracker.repository.LTLocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


class LocationUpdateWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitude = 0.0
    private var longitude = 0.0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
//        startForegroundService()
        getLastLocation()
        Log.d("TAG", "doWork123: ")

        return Result.success()
    }

    /***
     * to get the current location
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLastLocation() {

        // Permission is granted
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    Log.d("TAG", "getLastLocation: $location")
                    // Handle location update
                    location?.let {
                        latitude = it.latitude
                        longitude = it.longitude
                    }
                    val geocoder = Geocoder(applicationContext, Locale.getDefault())//using geo code to convert the address
                    val address = geocoder.getFromLocation(latitude, longitude, 1)
                    Log.d("TAG", "getLastLocation: $latitude$longitude" + "address$address")
                    if (!address.isNullOrEmpty()) {
                        val addressLine = address[0]?.getAddressLine(0)
                        val timeStamp = System.currentTimeMillis()
                        val formatTime = convertMillisToDateString(timeStamp)

                        val email = LTApplicationClass.sharedPreference.getMail()
                        LTLocationRepository.insertLocationDetails(
                            LocationData(
                                email.toString(),
                                latitude,
                                longitude,
                                addressLine.toString(),
                                formatTime
                            )
                        )
                    }
                    // Broadcast intent to notify the UI
                    val intent = Intent("location_updated")
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

                }
                .addOnFailureListener { e ->
                    // Handle failure
                    Log.e("LocationUpdateWorker", "Error getting location: ${e.message}")
                }
        } else Log.d("TAG", "getLastLocation: ")


    }

    /**
     * convert the millisecond to time and date
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertMillisToDateString(milliseconds: Long): String {
        val instant = Instant.ofEpochMilli(milliseconds)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("MMM.dd.yyyy HH:mm:ss")
        return formatter.format(localDateTime)
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(applicationContext, LocationForegroundService::class.java)
        ContextCompat.startForegroundService(applicationContext, serviceIntent)
    }
}

