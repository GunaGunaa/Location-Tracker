package com.example.locationtracker.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.locationtracker.databinding.LtActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class LTSplashActivity : AppCompatActivity() {
    private lateinit var binding: LtActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = LtActivitySplashBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.rippleBackground.startRippleAnimation()
        Handler().postDelayed({
            startActivity(Intent(this, LTAuthenticationActivity::class.java))
            finish()
//            binding.rippleBackground.stopRippleAnimation()
        }, 4000)
    }
}