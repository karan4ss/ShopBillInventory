package com.example.shopbillinventory

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.shopbillinventory.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var splashScreenBinding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding.root)
        val animationDuration = 2000L

        // Define the translation animation
        val translationAnimator = ObjectAnimator.ofFloat(
            splashScreenBinding.rvSplashScreenLogo,
            "translationY",
            -200f, // Start position
            50f // End position (translate up by 200 pixels)
        )

        // Set the animation duration
        translationAnimator.duration = animationDuration

        // Start the animation
        translationAnimator.start()
        Handler(Looper.getMainLooper()).postDelayed({

            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val loggedInBefore = sharedPreferences.getBoolean("loggedInBefore", false)
            if (loggedInBefore){
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

        }, 4000)
    }
}