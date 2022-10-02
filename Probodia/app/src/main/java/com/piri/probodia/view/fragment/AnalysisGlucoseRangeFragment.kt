package com.piri.probodia.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.piri.probodia.R
import com.piri.probodia.databinding.FragmentAnalysisRangeBinding
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.viewmodel.AnalysisRangeViewModel


class AnalysisGlucoseRangeFragment : Fragment() {

    private lateinit var binding : FragmentAnalysisRangeBinding

    private lateinit var viewModel : AnalysisRangeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalysisRangeBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(AnalysisRangeViewModel::class.java)
        binding.vm = viewModel

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (binding.firstLayout.width > 0) {
            setGlucoseRange()
        } else {
            binding.firstLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
                OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.firstLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    setGlucoseRange()
                }
            })
        }
    }

    fun setGlucoseRange() {
        val secondLayoutParams = binding.secondLayout.layoutParams
        secondLayoutParams.width = binding.firstLayout.width / 3 * 2
        binding.secondLayout.layoutParams = secondLayoutParams

        val thirdLayoutParams = binding.thirdLayout.layoutParams
        thirdLayoutParams.width = binding.firstLayout.width / 3
        binding.thirdLayout.layoutParams = thirdLayoutParams
    }
}