package com.inoomene.fizzbuzz.utils

import android.text.*
import androidx.core.text.HtmlCompat

 fun String.toColoredSpanned(color: String): String {
    return "<font color=$color>$this</font>"
}

fun Int.percent(total: Int) : String{
    val percent = toDouble()*100/total
    return String.format("%.1f", percent)
}

 fun CharSequence.add(other: String): CharSequence {
     return TextUtils.concat(this, other)
 }

 fun CharSequence.toHtmlFormat() : CharSequence {
    return HtmlCompat.fromHtml(this.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
 }
