package com.inoomene.fizzbuzz.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.inoomene.fizzbuzz.R
import com.inoomene.fizzbuzz.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding : MainFragmentBinding =  DataBindingUtil.inflate(
            inflater,
            R.layout.main_fragment,
            container,
            false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.mainViewModel = viewModel
        binding.result.movementMethod = ScrollingMovementMethod()
        viewModel.resultLiveData.observe(viewLifecycleOwner, {
            binding.result.text = colorKeyWords(it,
                binding.firstStringEditText.text.toString(),
                binding.secondStringEditText.text.toString())
        })
        return binding.root
    }

    private fun  colorKeyWords(allText:String, str1:String, str2:String) : Spanned {
        val firstVersion = allText.replace("$str1$str2", "<font color='#0000ee'>$str1$str2</font>")
        val secondVersion = firstVersion.replace(str1, "<font color='#EE0000'>$str1</font>")
        val finalVersion = secondVersion.replace(str2, "<font color='#00ee00'>$str2</font>")
        return HtmlCompat.fromHtml(finalVersion, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}