package com.setianjay.mydigitalent.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.ByteArrayOutputStream


object ViewUtil {
    /**
     * To get [Bitmap] from [Uri]
     *
     * @param resolver ContentResolver
     * @param uri Uri
     *
     * @author Hari Setiaji
     * @return [Bitmap]
     * */
    fun getBitmapFromUri(resolver: ContentResolver, uri: Uri): Bitmap {
        lateinit var bitmap: Bitmap

        try {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(resolver, uri)
            } else {
                val source = ImageDecoder.createSource(resolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bitmap
    }

    /**
     * To get [ByteArray] from [Bitmap]
     *
     * @param bitmap Bitmap
     * @return [ByteArray]
     * @author Hari Setiaji
     * */
    fun getByteArrayFromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.JPEG, 0, outputStream)
        return outputStream.toByteArray()
    }

    /**
     * To get [Bitmap] from [ByteArray]
     *
     * @param bytes ByteArray
     * @return [Bitmap]
     * @author Hari Setiaji
     * */
    fun getBitmapFromByteArray(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}