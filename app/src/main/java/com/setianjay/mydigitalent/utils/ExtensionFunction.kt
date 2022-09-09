package com.setianjay.mydigitalent.utils

import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.setianjay.mydigitalent.R
import java.util.*

/*---------------------VIEW------------------------*/

/**
 * To check whether [ImageView] has image
 *
 * @return [Boolean]
 * @author Hari Setiaji
 * */
fun ImageView.hasDefaultImage(): Boolean {
    return this.drawable.constantState == ResourcesCompat.getDrawable(
        resources,
        R.drawable.im_placeholder_user_profile,
        null
    )?.constantState
}

/**
 * To make 1 letter at the beginning of a sentence capitalized
 *
 * @return [String] result
 * @author Hari Setiaji
 * */
fun String.capitalFirstChar(): String {
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
}