package com.inoomene.fizzbuzz.utils

import android.text.*
import android.text.style.ForegroundColorSpan
import androidx.annotation.ColorInt
import androidx.core.text.HtmlCompat

fun String.toSpannableString(@ColorInt colorInt : Int): SpannableString{
    val spannableString = SpannableString(this)
    val foregroundSpan = ForegroundColorSpan(colorInt)
    spannableString.setSpan(
        foregroundSpan,
        0,
        spannableString.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}

 fun String.toColoredSpanned(color: String): String {
    return "<font color=$color>$this</font>"
}

fun Int.percent(total: Int) : Int{
    return this*100/total
}

 fun CharSequence.add(other: String): CharSequence {
     return TextUtils.concat(this, other)
 }

 fun CharSequence.toHtmlFormat() : CharSequence {
    return HtmlCompat.fromHtml(this.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
 }
