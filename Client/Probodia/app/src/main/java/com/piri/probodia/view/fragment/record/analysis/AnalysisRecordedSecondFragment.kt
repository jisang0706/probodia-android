package com.piri.probodia.view.fragment.record.analysis

class AnalysisRecordedSecondFragment : AnalysisRecordedRangeFragment() {

    override fun initChild() {
        binding.recordedTitleText.text = "점심"

        recordedViewModel.secGlucoseRange.observe(viewLifecycleOwner) {
            setGlucoseRange(14, it)
        }
    }
}