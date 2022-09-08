package com.setianjay.mydigitalent.utils

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.setianjay.mydigitalent.R

class Animations(private val context: Context) {

    fun logoAtSplashScreenAnimation(): Animation =
        AnimationUtils.loadAnimation(context, R.anim.logo_at_splash_screen)

    fun subtitleAtSplashScreenAnimation(): Animation =
        AnimationUtils.loadAnimation(context, R.anim.subtitle_at_splash_screen)
}