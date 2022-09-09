package com.setianjay.mydigitalent.models

import android.graphics.Bitmap
import com.setianjay.mydigitalent.enums.Gender

data class User(
    val id: Int,
    val username: String,
    val address: String,
    val phone: String,
    val gender: Gender,
    val password: String,
    val photo: Bitmap,
    val location: Location
){
    data class Location(val latitude: String, val longitude: String)
}
