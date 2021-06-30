package com.inoomene.fizzbuzz.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.annotation.ColorInt

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

fun Int.percent(total: Int) : Int{
    return this*100/total
}

 fun CharSequence.add(other: Spannable): CharSequence{
    return SpannableStringBuilder(this).append(other)
}