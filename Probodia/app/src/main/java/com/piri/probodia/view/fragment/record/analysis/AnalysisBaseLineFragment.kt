package com.piri.probodia.view.fragment.record.analysis

import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.piri.probodia.databinding.FragmentAnalysisLineBinding
import com.piri.probodia.viewmodel.RecordAnalysisViewModel

open class AnalysisBaseLineFragment : Fragment() {

    protected lateinit var binding : FragmentAnalysisLineBinding

    protected lateinit var viewModel : RecordAnalysisViewModel

    fun initChart() {
        binding.apply {
            analysisChart.description.isEnabled = false
            analysisChart.setMaxVisibleValueCount(999)
            analysisChart.setVisibleXRangeMaximum(7f)
            analysisChart.setPinchZoom(false)
            analysisChart.setDrawGridBackground(false)
            analysisChart.isDragEnabled = false
            analysisChart.isDoubleTapToZoomEnabled = false
            analysisChart.setNoDataText("기간 내 기록이 존재하지 않습니다.")

            analysisChart.legend.apply {
                form = Legend.LegendForm.NONE
            }

            analysisChart.xAxis.apply {
//                textColor = Color.TRANSPARENT
                position = XAxis.XAxisPosition.BOTTOM
                axisMinimum = 1f
                axisMaximum = 7.5f
                setLabelCount(6, false)

                this.setDrawGridLines(false)
            }

            analysisChart.axisLeft.apply {
                setLabelCount(10, false)
                axisMinimum = 0f
                axisMaximum = 200f
//                textColor = Color.WHITE
                setDrawGridLines(true)
                setDrawAxisLine(true)
            }

            analysisChart.axisRight.apply {
                isEnabled = false
            }
        }
    }
}