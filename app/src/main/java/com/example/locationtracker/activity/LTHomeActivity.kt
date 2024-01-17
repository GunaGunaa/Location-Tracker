package com.example.locationtracker.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.locationtracker.Constants
import com.example.locationtracker.LTApplicationClass
import com.example.locationtracker.R
import com.example.locationtracker.adapter.LocationAdapter
import com.example.locationtracker.common.AlertDialogHelper
import com.example.locationtracker.databinding.LtActivityHomeBinding
import com.example.locationtracker.fragment.LTUsersBottomSheetFragment
import com.example.locationtracker.listener.LocationTouchListener
import com.example.locationtracker.listener.SwitchUserItemClickListener
import com.example.locationtracker.model.LatLongData
import com.example.locationtracker.model.LocationData
import com.example.locationtracker.repository.LTLocationRepository
import com.example.locationtracker.service.LocationForegroundService
import com.example.locationtracker.service.LocationUpdateWorker
import com.example.locationtracker.viewmodel.LTHomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

class LTHomeActivity : AppCompatActivity(), LocationTouchListener, SwitchUserItemClickListener {
    private lateinit var binding: LtActivityHomeBinding
    private lateinit var viewModel: LTHomeViewModel
    private lateinit var adapter: LocationAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = LtActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel = ViewModelProvider(this)[LTHomeViewModel::class.java]
        viewModel.getAllLocationList(LTApplicationClass.sharedPreference.getMail().toString())
        createNotificationChannel()
        getLocationPermission()
        locationListener()
        binding.tvUserName.text = LTApplicationClass.sharedPreference.getMail()


        val receiver = object : BroadcastReceiver() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onReceive(context: Context?, intent: Intent?) {
                // Update the RecyclerView with the latest data from the Realm database
                viewModel.getAllLocationList(
                    LTApplicationClass.sharedPreference.getMail().toString()
                )
                adapter.notifyDataSetChanged()
            }
        }



        val filter = IntentFilter("location_updated")
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)

        binding.llSwitchUser.setOnClickListener {
            val usersFragment = LTUsersBottomSheetFragment(this)
            usersFragment.show(supportFragmentManager, "UsersFragment")
        }

        binding.fabAddLocation.setOnClickListener {
            addLocation()
        }


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialogHelper.showAlertDialog(
                    this@LTHomeActivity,
                    "Exit",
                    "Are you sure want to exit",
                    null
                ) { _, _ ->
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        binding.tvLogout.setOnClickListener{
            AlertDialogHelper.showAlertDialog(
                this@LTHomeActivity,
                "Logout",
                "Are you sure want to Logout",
                null
            ) { _, _ ->
                LTApplicationClass.sharedPreference.setLoginStatus(false)
                startActivity(Intent(this,LTAuthenticationActivity::class.java))
                finish()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
           startForegroundService()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_CODE
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun locationListener() {
        viewModel.locationList.observe(this, Observer {
            if (it != null) {
                binding.rvLocation.layoutManager = LinearLayoutManager(this)
                adapter = LocationAdapter(it, this)
                binding.rvLocation.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              startForegroundService()
            }
        }
    }


    companion object {
        const val REQUEST_LOCATION_PERMISSION_CODE = 1
    }

    override fun onLocationTouchClickListener(
        locationList: ArrayList<LocationData>,
        currentLocation: LocationData
    ) {
        val latLongList: ArrayList<LatLongData> = ArrayList()
        for (latLong in locationList) {
            latLongList.add(LatLongData(latLong.latitude, latLong.longitude))
        }

        val intent = Intent(this, LTGoogleMapActivity::class.java)
        intent.putParcelableArrayListExtra(Constants.LAT_LONG_LIST, latLongList)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun getSwitchUSerItemClickListener(email: String) {
        viewModel.getAllLocationList(email)
        adapter.notifyDataSetChanged()
        binding.tvUserName.text = email
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Location Tracker"
            val descriptionText = "Location updates notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(LocationForegroundService.CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundService() {
        if (!foregroundServiceRunning()) {
            val serviceIntent = Intent(this, LocationForegroundService::class.java)
            startForegroundService(serviceIntent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addLocation() {
        var latitude: Double = 0.0
        var longitude: Double = 0.0

        // Permission is granted
        val fusedLocationProviderClient =
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
                    val geocoder = Geocoder(applicationContext, Locale.getDefault())
                    val address = geocoder.getFromLocation(latitude, longitude, 1)
                    Log.d("TAG", "getLastLocation: $latitude$longitude")
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
                        // Broadcast intent to notify the UI
                        val intent = Intent("location_updated")
                        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                    } else Log.d("getLastLocation:", "latitude error ")

                }
                .addOnFailureListener { e ->
                    // Handle failure
                    Log.e("LocationUpdateWorker", "Error getting location: ${e.message}")
                }
        } else Log.d("TAG", "getLastLocation: ")


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertMillisToDateString(milliseconds: Long): String {
        val instant = Instant.ofEpochMilli(milliseconds)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("MMM.dd.yyyy HH:mm:ss")
        return formatter.format(localDateTime)
    }

    /**
     * to check the service is running or not
     */
    private fun foregroundServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (LocationForegroundService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

}