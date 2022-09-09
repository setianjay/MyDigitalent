package com.setianjay.mydigitalent.presentations

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    }

    private fun initAnimation() {
        binding.apply {
            ivApp.startAnimation(animations.logoAtSplashScreenAnimation())
            tvSubtitle1.startAnimation(animations.subtitleAtSplashScreenAnimation())

            val endOfAnimation = animations.subtitleAtSplashScreenAnimation()
            endOfAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    checkPermissionApp()
                }

            })
            tvSubtitle2.startAnimation(endOfAnimation)
        }
    }

    private fun moveToLoginScreen() {
        Intent(this, RegisterActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun checkPermissionApp() {
        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

        if (ActivityCompat.checkSelfPermission(this, locationPermission) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(locationPermission), APP_PERMISSION)
        } else {
            moveToLoginScreen()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == APP_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                moveToLoginScreen()
            }else{
                checkPermissionApp()
            }
        }
    }

    companion object {
        private const val APP_PERMISSION = 0
    }
}