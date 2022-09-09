package com.setianjay.mydigitalent.utils

object Constant {

    object DATABASE{
        // DATABASE
        const val DATABASE_NAME = "digitalent_db"
        const val DATABASE_VERSION = 1

        // TABLE USER
        const val TABLE_USER = "user"
        const val KEY_USER_ID = "id"
        const val KEY_USER_USERNAME = "username"
        const val KEY_USER_ADDRESS = "address"
        const val KEY_USER_PHONE = "phone"
        const val KEY_USER_GENDER = "gender"
        const val KEY_USER_PASSWORD = "password"
        const val KEY_USER_PHOTO = "photo"
        const val KEY_USER_LATITUDE = "latitude"
        const val KEY_USER_LONGITUDE = "longitude"
    }

    object FormInput{
        const val ADDRESS = "address"
        const val PASSWORD = "password"
        const val PHONE = "phone"
        const val USERNAME = "username"
    }

    object Gender{
        const val MALE = "MALE"
        const val FEMALE = "FEMALE"
    }
}