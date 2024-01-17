package com.example.locationtracker.model

import io.realm.RealmObject

open class LocationData(
    var email: String="",
    var latitude: Double=0.0,
    var longitude: Double=0.0,
    var address: String="",
    var timestamp: String = ""
) : RealmObject()