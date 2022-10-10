package com.piri.probodia.view.fragment

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.TodayRecord
import com.piri.probodia.databinding.FragmentAnalysisLineBinding
import com.piri.probodia.viewmodel.RecordAnalysisViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class AnalysisPressureChartFragment : AnalysisBaseLineFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalysisLineBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(requireParentFragment()).get(RecordAnalysisViewModel::class.java)
        binding.vm = viewModel

        binding.analysisLineText.text = "혈압 추세선"

       initChart()

        viewModel.pressureResult.observe(viewLifecycleOwner) {
            binding.analysisChart.apply {
                var combinedData = CombinedData()
                val candleData = setPressureCandleStick(it)
                if (candleData != null) {
                    combinedData.setData(candleData)
                    this.data = combinedData
                    this.invalidate()
                }
            }
        }

        return binding.root
    }

    private fun setPressureCandleStick(items : MutableList<TodayRecord.AllData>) : CandleData? {
        val candleEntries = buildList<CandleEntry> {
            for (item in items) {
                val localdate = LocalDate.parse(item.record.recordDate.split(' ')[0])
                var x = (viewModel.kindEndDate.value!!.second.until(
                    localdate,
                    ChronoUnit.DAYS
                )).toFloat() + 8

                add(CandleEntry(
                    x,
                    item.record.maxPressure!!.toFloat(),
                    item.record.minPressure!!.toFloat(),
                    item.record.maxPressure!!.toFloat(),
                    item.record.minPressure!!.toFloat()
                ))
            }
        }

        return if (candleEntries.isNotEmpty()) {
            CandleData(buildList {
                add(CandleDataSet(candleEntries, null).apply {
                    decreasingColor = ResourcesCompat.getColor(resources, R.color.red_800, null)
                    decreasingPaintStyle = Paint.Style.FILL
                    axisDependency = YAxis.AxisDependency.LEFT
                    valueTextColor = Color.TRANSPARENT
                })
            })
        } else {
            null
        }
    }
}