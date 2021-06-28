package com.inoomene.fizzbuzz.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return binding.root
    }


}