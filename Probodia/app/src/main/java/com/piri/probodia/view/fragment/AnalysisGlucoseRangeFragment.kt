package com.piri.probodia.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.piri.probodia.R
import com.piri.probodia.databinding.FragmentAnalysisRangeBinding
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.data.remote.model.GlucoseDto
import com.piri.probodia.data.remote.model.RecordDatasBase
import com.piri.probodia.data.remote.model.TodayRecord
import com.piri.probodia.viewmodel.RecordAnalisysViewModel


class AnalysisGlucoseRangeFragment : Fragment() {

    private lateinit var binding : FragmentAnalysisRangeBinding

    private lateinit var viewModel : RecordAnalisysViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalysisRangeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(requireParentFragment()).get(RecordAnalisysViewModel::class.java)
        binding.vm = viewModel

        viewModel.glucoseRange.observe(viewLifecycleOwner) {
            setGlucoseRange(it[0], it[1], it[2])
            binding.firstText.text = "${it[0]}"
            binding.secondText.text = "${it[1]}"
            binding.thirdText.text = "${it[2]}"
        }

        return binding.root
    }

    fun setGlucoseRange(common : Int, low : Int, high : Int) {
        if (binding.firstLayout.width > 0) {
            _setGlucoseRange(common, low, high)
        } else {
            binding.firstLayout.viewTreeObserver.addOnGlobalLayoutListener(
                object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.firstLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    _setGlucoseRange(common, low, high)
                }
            })
        }
    }

    fun _setGlucoseRange(common : Int, low : Int, high : Int) {

        if (common == 0) {
            binding.secondLayout.setBackgroundResource(R.drawable.yellow_600_round_background)
            if (low == 0) {
                binding.thirdLayout.setBackgroundResource(R.drawable.red_600_round_background)
            }
        }
        val thirdLayoutParams = binding.thirdLayout.layoutParams
        thirdLayoutParams.width = binding.firstLayout.width * high / (common + low + high)
        binding.thirdLayout.layoutParams = thirdLayoutParams

        val secondLayoutParams = binding.secondLayout.layoutParams
        secondLayoutParams.width =
            binding.firstLayout.width * low / (common + low + high) + thirdLayoutParams.width
        binding.secondLayout.layoutParams = secondLayoutParams
    }
}