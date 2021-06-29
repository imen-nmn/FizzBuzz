package com.inoomene.fizzbuzz.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.inoomene.fizzbuzz.R
import com.inoomene.fizzbuzz.data.FizzBuzzWarning
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

        viewModel.warningLiveData.observe(viewLifecycleOwner, {
            when (it) {
                FizzBuzzWarning.CLOSE_KEYBOARD -> {
                    closeKeyboard()
                }
                FizzBuzzWarning.NO_FIELD_FILLED -> {
                    showSnackBar(getString(R.string.fill_fields_required))
                }
                FizzBuzzWarning.VERY_BIG_LIMIT -> {
                    showSnackBar(getString(R.string.very_big_limit))
                }
                null -> return@observe
            }
        })
        return binding.root
    }

    private fun showSnackBar(msg: String) {
        val snackBar = view?.let { Snackbar.make(requireContext(),
            it,
            msg,
            Snackbar.LENGTH_INDEFINITE)
        }
        snackBar?.setAction(android.R.string.ok) {
            snackBar.dismiss()
        }
        snackBar?.show()
    }

    private fun closeKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}