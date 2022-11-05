package com.piri.probodia.view.fragment.record.analysis

import android.util.Log

class AnalysisRecordedFirstFragment : AnalysisRecordedRangeFragment() {

    override fun initChild() {
        binding.recordedTitleText.text = "아침"

        recordedViewModel.firGlucoseRange.observe(viewLifecycleOwner) {
            Log.e("RECORDED", it.toString())
            setGlucoseRange(14, it)
        }
    }
}