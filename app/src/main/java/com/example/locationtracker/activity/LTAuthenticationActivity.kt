package com.example.locationtracker.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.locationtracker.R
import com.example.locationtracker.fragment.LTLoginFragment

class LTAuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lt_activity_authentication)
        supportFragmentManager.beginTransaction().replace(R.id.fl_authentication, LTLoginFragment())
           .commit()
    }
}