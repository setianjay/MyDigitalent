package com.setianjay.mydigitalent.presentations

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.setianjay.mydigitalent.databinding.ActivitySplashBinding
import com.setianjay.mydigitalent.utils.Animations

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val animations: Animations by lazy { Animations(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAnimation()
        moveToLoginScreen()
    }

    private fun initAnimation(){
        binding.apply {
            ivApp.startAnimation(animations.logoAtSplashScreenAnimation())
            tvSubtitle1.startAnimation(animations.subtitleAtSplashScreenAnimation())
            tvSubtitle2.startAnimation(animations.subtitleAtSplashScreenAnimation())
        }
    }

    private fun moveToLoginScreen(){
        Handler().postDelayed({
            Toast.makeText(this, "Move to activity login", Toast.LENGTH_SHORT).show()
        }, SPLASH_DURATION)
    }

    companion object{
        private const val SPLASH_DURATION = 1800L
    }
}