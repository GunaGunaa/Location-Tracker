package com.example.locationtracker



import android.app.Application
import android.content.Context
import com.example.locationtracker.common.LTSharedPreferences
import io.realm.Realm


class LTApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        sharedPreference = LTSharedPreferences(this)
        appContext = this
        Realm.init(this)
    }

    companion object {
        lateinit var sharedPreference: LTSharedPreferences
            private set
        lateinit var appContext: Context
            private set
    }
}