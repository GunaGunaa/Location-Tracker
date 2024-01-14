package com.example.locationtracker.model

import io.realm.RealmObject

open class UserRealmModel(
    var name: String = "",
    var mail: String = "",
    var password: String = ""
) : RealmObject()