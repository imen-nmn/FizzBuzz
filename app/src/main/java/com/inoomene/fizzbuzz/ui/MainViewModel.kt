package com.inoomene.fizzbuzz.ui

import android.text.SpannableString
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inoomene.fizzbuzz.data.FizzBuzzWarning
import com.inoomene.fizzbuzz.data.Output
import com.inoomene.fizzbuzz.utils.add
import com.inoomene.fizzbuzz.utils.percent
import com.inoomene.fizzbuzz.utils.toColoredSpanned
import com.inoomene.fizzbuzz.utils.toHtmlFormat
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
    var commentLiveData = MutableLiveData<CharSequence>()
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
    ): Output {
        return if (limit > BIG_NUMBER)
            applyFizzWithoutColor(int1, int2, limit, str1, str2)
        else
            applyFizzWithColor(int1, int2, limit, str1, str2)

    }

    private fun applyFizzWithoutColor(
        int1: Int,
        int2: Int,
        limit: Int,
        str1: String,
        str2: String
    ): Output {
        var firstStrOccurrence = 0
        var secondStrOccurrence = 0
        var concatStrOccurrence = 0
        var sequence: CharSequence = StringBuffer()
        for (i in 1..limit) {
            sequence = if (i % int1 == 0 && i % int2 == 0) {
                concatStrOccurrence++
                "$sequence $str1$str2"
            } else if (i % int2 == 0) {
                secondStrOccurrence++
                "$sequence $str2"
            } else if (i % int1 == 0) {
                firstStrOccurrence++
                "$sequence $str1"
            } else {
                "$sequence $i"
            }

            if (i % 1000 == 0)
                setResultProgressingInMain("${i.percent(limit)}% Processing .. $i from $limit")
        }
        return Output(
            sequence.trim().toHtmlFormat(),
            firstStrOccurrence,
            secondStrOccurrence,
            concatStrOccurrence
        )
    }

    private fun applyFizzWithColor(
        int1: Int,
        int2: Int,
        limit: Int,
        str1: String,
        str2: String
    ): Output {
        var sequence: CharSequence = StringBuffer()
        var firstStrOccurrence = 0
        var secondStrOccurrence = 0
        var concatStrOccurrence = 0
        for (i in 1..limit) {
            if (i % int1 == 0 && i % int2 == 0) {
                sequence = sequence.add(" $str1$str2".toColoredSpanned("#fcba03"))
                concatStrOccurrence++
            } else if (i % int2 == 0) {
                sequence = sequence.add(" $str2".toColoredSpanned("#dc3e7f"))
                secondStrOccurrence++
            } else if (i % int1 == 0) {
                sequence = sequence.add(" $str1".toColoredSpanned("#ff1515"))
                firstStrOccurrence++
            } else {
                sequence = sequence.add(" $i".toColoredSpanned("#ffffff"))
            }

            if (i % 1000 == 0)
                setResultProgressingInMain("${i.percent(limit)}% Processing .. $i from $limit")
        }

        return Output(
            sequence.trim().toHtmlFormat(),
            firstStrOccurrence,
            secondStrOccurrence,
            concatStrOccurrence
        )
    }


    private fun setResultLiveDataInMain(output: Output) {
        viewModelScope.launch {
            showResult.value = true
            resultLiveData.value = output.resultText
            commentLiveData.value = displayStatistic(
                output.firstStrOccurrence,
                output.secondStrOccurrence,
                output.concatStrOccurrence
            )
            loadingResult.value = false
        }
    }

    private fun setResultProgressingInMain(progress: String) {
        viewModelScope.launch {
            showResult.value = true
            commentLiveData.value = SpannableString(progress)
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
            if (limit > BIG_NUMBER) {
                warningLiveData.value = FizzBuzzWarning.VERY_BIG_LIMIT
            }
        }
    }

    private fun displayOccurrence(key: String?, occurrence: Int, total: Int?): String {
        return "${total?.let { occurrence.percent(it) }}% of $key : $occurrence from $total in total "
    }

    private fun displayStatistic(
        firstNumber: Int,
        secondNumber: Int,
        concatNumber: Int
    ): CharSequence {
        val limit = limitIntLiveData.value?.toInt()
        val firstStr = firstStringLiveData.value
        val secondStr = secondStringLiveData.value
        val firstStatistic = displayOccurrence(firstStr, firstNumber, limit)
        val secondStatistic = displayOccurrence(secondStr, secondNumber, limit)
        val concatStatistic = displayOccurrence("$firstStr$secondStr", concatNumber, limit)
        return "$firstStatistic\n\n$secondStatistic\n\n$concatStatistic"
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    companion object {
        const val BIG_NUMBER = 30000
    }

}

