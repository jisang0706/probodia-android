package com.piri.probodia.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.databinding.FragmentAnalysisRecordedRangeBinding
import com.piri.probodia.viewmodel.AnalysisRecordedViewModel

class AnalysisRecordedSecondFragment : AnalysisRecordedRangeFragment() {

    override fun initChild() {
        binding.recordedTitleText.text = "점심"

        recordedViewModel.secGlucoseRange.observe(viewLifecycleOwner) {
            setGlucoseRange(14, it)
        }
    }
}