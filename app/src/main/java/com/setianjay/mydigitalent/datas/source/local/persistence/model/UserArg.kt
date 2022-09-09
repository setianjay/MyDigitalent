package com.setianjay.mydigitalent.datas.source.local.persistence.model

import android.graphics.Bitmap
import com.setianjay.mydigitalent.enums.Gender
import com.setianjay.mydigitalent.models.User

data class UserArg(
    val username: String,
    val address: String,
    val phone: String,
    val gender: Gender,
    val password: String,
    val photo: Bitmap,
    val location: User.Location
)