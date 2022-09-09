package com.setianjay.mydigitalent.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.lang.Exception

object ViewUtil {
    /**
     * To get Bitmap from Uri
     *
     * @param resolver Content Resolver
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
}