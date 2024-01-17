package com.example.locationtracker.common



import android.content.Context
import android.content.SharedPreferences

class LTSharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences
    private var loginStatus = "Login Status"
    private var fromLogin = "From Login"
    private var gmail="gmail"
    private var name="name"

    init {
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    fun setMail( value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(gmail, value)
        editor.apply()
    }

    fun getMail(): String? {
        return sharedPreferences.getString(gmail, "")
    }

    fun setLoginStatus(status: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(loginStatus, status!!)
        editor.apply()
    }

    fun getLoginStatus(): Boolean {
        return sharedPreferences.getBoolean(loginStatus, false)
    }

    fun setName(value:String){
        val editor = sharedPreferences.edit()
        editor.putString(name, value)
        editor.apply()
    }

    fun getName():String?{
        return sharedPreferences.getString(name,"")
    }



}