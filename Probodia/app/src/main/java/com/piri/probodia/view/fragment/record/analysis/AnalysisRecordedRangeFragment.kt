package com.piri.probodia.view.fragment.record.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.databinding.FragmentAnalysisRecordedRangeBinding
import com.piri.probodia.viewmodel.AnalysisRecordedViewModel

open class AnalysisRecordedRangeFragment : Fragment() {

    protected lateinit var binding : FragmentAnalysisRecordedRangeBinding

    protected lateinit var recordedViewModel : AnalysisRecordedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalysisRecordedRangeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        recordedViewModel = ViewModelProvider(requireParentFragment()).get(AnalysisRecordedViewModel::class.java)
        binding.recordedVm = recordedViewModel

        initChild()

        return binding.root
    }

    open fun initChild() {}

    protected fun setGlucoseRange(all : Int, recorded : Int) {
        binding.recordedPercentText.text = "${recorded * 100 / all}% 기록"

        if (binding.firstLayout.width > 0) {
            _setGlucoseRange(all, recorded)
        } else {
            binding.firstLayout.viewTreeObserver.addOnGlobalLayoutListener (
                object  : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        binding.firstLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        _setGlucoseRange(all, recorded)
                    }
                }
            )
        }
    }

    private fun _setGlucoseRange(all : Int, recorded : Int) {
        if (recorded == 0) {
            binding.secondLayout.setBackgroundResource(R.drawable.primary_100_round_background)
        } else {
            binding.secondLayout.setBackgroundResource(R.drawable.primary_100_round_right_background)
        }

        val secondLayoutParams = binding.secondLayout.layoutParams
        secondLayoutParams.width =
            binding.firstLayout.width * (all - recorded) / all
        binding.secondLayout.layoutParams = secondLayoutParams
    }
}