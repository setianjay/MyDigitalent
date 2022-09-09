package com.setianjay.mydigitalent.datas.source.local.persistence.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.setianjay.mydigitalent.datas.source.local.persistence.model.UserArg
import com.setianjay.mydigitalent.enums.Gender
import com.setianjay.mydigitalent.models.User
import com.setianjay.mydigitalent.models.User.Location
import com.setianjay.mydigitalent.utils.Constant.DATABASE.DATABASE_NAME
import com.setianjay.mydigitalent.utils.Constant.DATABASE.DATABASE_VERSION
import com.setianjay.mydigitalent.utils.Constant.DATABASE.KEY_USER_ADDRESS
import com.setianjay.mydigitalent.utils.Constant.DATABASE.KEY_USER_GENDER
import com.setianjay.mydigitalent.utils.Constant.DATABASE.KEY_USER_ID
import com.setianjay.mydigitalent.utils.Constant.DATABASE.KEY_USER_LATITUDE
import com.setianjay.mydigitalent.utils.Constant.DATABASE.KEY_USER_LONGITUDE
import com.setianjay.mydigitalent.utils.Constant.DATABASE.KEY_USER_PASSWORD
import com.setianjay.mydigitalent.utils.Constant.DATABASE.KEY_USER_PHONE
import com.setianjay.mydigitalent.utils.Constant.DATABASE.KEY_USER_PHOTO
import com.setianjay.mydigitalent.utils.Constant.DATABASE.KEY_USER_USERNAME
import com.setianjay.mydigitalent.utils.Constant.DATABASE.TABLE_USER

class SqliteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_USER($KEY_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_USER_USERNAME TEXT, $KEY_USER_ADDRESS TEXT, $KEY_USER_PHONE TEXT, $KEY_USER_GENDER TEXT, $KEY_USER_PASSWORD TEXT, $KEY_USER_PHOTO TEXT, $KEY_USER_LATITUDE TEXT, $KEY_USER_LONGITUDE TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    fun addUser(userArg: UserArg): Long{
        val result: Long

        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_USER_USERNAME, userArg.username)
            put(KEY_USER_ADDRESS, userArg.address)
            put(KEY_USER_PHONE, userArg.phone)
            put(KEY_USER_GENDER, userArg.gender.value)
            put(KEY_USER_PASSWORD, userArg.password)
            put(KEY_USER_PHOTO, userArg.photoPath)
            put(KEY_USER_LATITUDE, userArg.location.latitude)
            put(KEY_USER_LONGITUDE, userArg.location.longitude)
        }
        result = db.insert(TABLE_USER, null, contentValues)
        db.close()

        return result
    }

    @SuppressLint("Recycle")
    fun getUserByUsername(username: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USER WHERE username = ?", arrayOf(username))
        if(cursor.moveToFirst()) {
            return User(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                Gender.valueOf(cursor.getString(4)),
                cursor.getString(5),
                cursor.getString(6),
                Location(cursor.getString(7), cursor.getString(8))
            )
        }

        return null
    }

    fun isUserExist(username: String): Boolean{
        if(getUserByUsername(username) != null){
            return true
        }
        return false
    }
}