package com.example.locationtracker.listener

import com.example.locationtracker.model.LocationData

interface LocationTouchListener {
    fun onLocationTouchClickListener(locationList:ArrayList<LocationData>,currentLocation:LocationData)
}