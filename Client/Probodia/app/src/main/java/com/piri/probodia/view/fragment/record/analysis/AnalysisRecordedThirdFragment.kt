package com.piri.probodia.view.fragment.record.analysis

class AnalysisRecordedThirdFragment : AnalysisRecordedRangeFragment() {

    override fun initChild() {
        binding.recordedTitleText.text = "저녁"

        recordedViewModel.thrGlucoseRange.observe(viewLifecycleOwner) {
            setGlucoseRange(14, it)
        }
    }
}