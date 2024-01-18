package com.example.locationtracker.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.locationtracker.LTApplicationClass
import com.example.locationtracker.databinding.LtActivitySplashBinding
import io.realm.Realm

@SuppressLint("CustomSplashScreen")
class LTSplashActivity : AppCompatActivity() {
    private lateinit var binding: LtActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = LtActivitySplashBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.rippleBackground.startRippleAnimation()
        Handler().postDelayed({
            if (LTApplicationClass.sharedPreference.getLoginStatus()) {
                startActivity(Intent(this, LTHomeActivity::class.java))
            } else {
                startActivity(Intent(this, LTAuthenticationActivity::class.java))
            }
            finish()
        }, 4000)
    }
}