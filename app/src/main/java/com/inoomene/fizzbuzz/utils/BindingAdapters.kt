package com.inoomene.fizzbuzz.utils

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

@BindingAdapter("android:visible")
fun setVisible(view: View, visible: Boolean) {
    when {
        visible -> view.visibility = View.VISIBLE
        view is ProgressBar -> view.postDelayed({view.visibility = View.GONE}, 300)
        else -> view.visibility = View.GONE
    }
}