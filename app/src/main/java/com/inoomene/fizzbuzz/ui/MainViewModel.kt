package com.inoomene.fizzbuzz.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
   var firstIntLiveData = MutableLiveData<String>()
   var secondIntLiveData = MutableLiveData<String>()
   var limitIntLiveData = MutableLiveData<String>()
   var firstStringLiveData = MutableLiveData<String>()
   var secondStringLiveData = MutableLiveData<String>()
   var resultLiveData = MutableLiveData<String>()


    fun onLaunchResultClicked(){
      resultLiveData.value = ""
      val msg = "int1 : ${firstIntLiveData.value} , "+
              "int2 : ${secondIntLiveData.value} ,"+
              "limit : ${limitIntLiveData.value} \n"+
              "str1 : ${firstStringLiveData.value} \n"+
              "str2 : ${secondStringLiveData.value} \n\n"

      if (checkAllFieldFilled()) {

          val int1 = firstIntLiveData.value?.toIntOrNull() ?: 0
          val int2 = secondIntLiveData.value?.toIntOrNull() ?: 0
          val limit = limitIntLiveData.value?.toIntOrNull() ?: 0
          val str1 = firstStringLiveData.value ?: ""
          val str2 = secondStringLiveData.value ?: ""

          resultLiveData.value = msg+applyFizzBuzz(int1, int2, limit , str1, str2 )
      }
      else
         resultLiveData.value = "Please fill all the fields !! "
   }


   private fun checkAllFieldFilled() : Boolean {
      if (firstIntLiveData.value.isNullOrEmpty()) return false
      if (secondIntLiveData.value.isNullOrEmpty()) return false
      if (limitIntLiveData.value.isNullOrEmpty()) return false
      if (firstStringLiveData.value.isNullOrEmpty()) return false
      if (secondStringLiveData.value.isNullOrEmpty()) return false
      return true
   }

    private fun applyFizzBuzz( int1: Int,
                               int2: Int,
                               limit : Int,
                               str1 : String,
                               str2:String) : String{
        var result = ""

        for (i in 1 .. limit){
            result = if (i%int1 == 0 && i%int2 == 0)
                "$result, $str1$str2"
            else if (i%int2==0)
                "$result, $str2"
            else if (i%int1==0)
                "$result, $str1"
            else
                "$result, $i"
        }
        return result.removePrefix(", ")
    }


}