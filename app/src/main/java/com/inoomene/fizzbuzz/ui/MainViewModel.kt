package com.inoomene.fizzbuzz.ui

import android.graphics.Color
import android.text.SpannableString
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inoomene.fizzbuzz.data.FizzBuzzWarning
import com.inoomene.fizzbuzz.utils.add
import com.inoomene.fizzbuzz.utils.toSpannableString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    var firstIntLiveData = MutableLiveData<String>()
    var secondIntLiveData = MutableLiveData<String>()
    var limitIntLiveData = MutableLiveData<String>()
    var firstStringLiveData = MutableLiveData<String>()
    var secondStringLiveData = MutableLiveData<String>()
    var resultLiveData = MutableLiveData<CharSequence>()
    var loadingResult: MutableLiveData<Boolean> = MutableLiveData(false)
    var showResult: MutableLiveData<Boolean> = MutableLiveData(false)
    var warningLiveData: MutableLiveData<FizzBuzzWarning> = MutableLiveData()
    private var job: Job? = null


    fun onLaunchResultClicked() {
        beforeProcess()
        if (checkAllFieldFilled()) {
            loadingResult.value = true
            doProcess()
        } else {
            warningLiveData.value = FizzBuzzWarning.NO_FIELD_FILLED
        }
    }

    private fun beforeProcess() {
        showResult.value = false
        resultLiveData.value = SpannableString("")
        warningLiveData.value = FizzBuzzWarning.CLOSE_KEYBOARD
        job?.cancel()
    }

    private fun doProcess() {
            job = CoroutineScope(Dispatchers.Default).launch {
                val int1 = firstIntLiveData.value?.toIntOrNull() ?: 0
                val int2 = secondIntLiveData.value?.toIntOrNull() ?: 0
                val limit = limitIntLiveData.value?.toIntOrNull() ?: 0
                val str1 = firstStringLiveData.value ?: ""
                val str2 = secondStringLiveData.value ?: ""
                checkLimit(limit)
                val result = applyFizzBuzz(int1, int2, limit, str1, str2)
                setResultLiveDataInMain(result)
            }
    }

    private fun applyFizzBuzz(
        int1: Int,
        int2: Int,
        limit: Int,
        str1: String,
        str2: String
    ): CharSequence {
        var result: CharSequence = StringBuffer()
        for (i in 1..limit) {
            result = if (i % int1 == 0 && i % int2 == 0)
                result.add(" $str1$str2".toSpannableString(Color.MAGENTA))
            else if (i % int2 == 0)
                result.add(" $str2".toSpannableString(Color.BLUE))
            else if (i % int1 == 0)
                result.add(" $str1".toSpannableString(Color.GREEN))
            else
                result.add(" $i".toSpannableString(Color.WHITE))

            setResultProgressingInMain("${100 * i / limit}% Processing .. $i from $limit")
        }
        return result.trim()
    }


    private fun setResultLiveDataInMain(result: CharSequence) {
        CoroutineScope(Dispatchers.Main.immediate).launch {
            showResult.value = true
            resultLiveData.value = result
            loadingResult.value = false
        }
    }

    private fun setResultProgressingInMain(progress: String) {
        CoroutineScope(Dispatchers.Main.immediate).launch {
            showResult.value = true
            resultLiveData.value = SpannableString(progress)
        }
    }

    private fun checkAllFieldFilled(): Boolean {
        if (firstIntLiveData.value.isNullOrEmpty()) return false
        if (secondIntLiveData.value.isNullOrEmpty()) return false
        if (limitIntLiveData.value.isNullOrEmpty()) return false
        if (firstStringLiveData.value.isNullOrEmpty()) return false
        if (secondStringLiveData.value.isNullOrEmpty()) return false
        return true
    }

    private fun checkLimit(limit: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            if (limit > 40000) {
                warningLiveData.value = FizzBuzzWarning.VERY_BIG_LIMIT
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}

