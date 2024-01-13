package com.example.locationtracker.common



import android.content.Context
import android.content.SharedPreferences

class LTSharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences
    private var loginStatus = "Login Status"
    private var fromLogin = "From Login"
    private var fromVip = "from Vip"

    init {
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    fun saveString(key: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun setLoginStatus(status: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(loginStatus, status!!)
        editor.apply()
    }

    fun getLoginStatus(): Boolean {
        return sharedPreferences.getBoolean(loginStatus, false)
    }

    fun setFromLogin(status: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(fromLogin, status!!)
        editor.apply()
    }

    fun getFromLogin(): Boolean {
        return sharedPreferences.getBoolean(fromLogin, false)
    }

    fun setIsFromVip(status: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(fromVip, status)
        editor.apply()
    }

    fun getIsFromVip(): Boolean {
        return sharedPreferences.getBoolean(fromVip, false)
    }
}