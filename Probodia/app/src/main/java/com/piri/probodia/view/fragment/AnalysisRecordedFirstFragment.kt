package com.piri.probodia.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.databinding.FragmentAnalysisRecordedRangeBinding
import com.piri.probodia.viewmodel.AnalysisRecordedViewModel

class AnalysisRecordedFirstFragment : AnalysisRecordedRangeFragment() {

    override fun initChild() {
        binding.recordedTitleText.text = "아침"

        recordedViewModel.firGlucoseRange.observe(viewLifecycleOwner) {
            Log.e("RECORDED", it.toString())
            setGlucoseRange(14, it)
        }
    }
}