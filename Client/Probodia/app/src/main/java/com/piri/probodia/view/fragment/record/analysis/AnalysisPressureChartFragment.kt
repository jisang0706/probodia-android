package com.piri.probodia.view.fragment.record.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
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

        binding.glucoseColorTextLayout.visibility = View.GONE

        initChart()

        viewModel.pressureResult.observe(viewLifecycleOwner) {
            it.sortBy { it.record.recordDate }
            binding.analysisChart.apply {
                var combinedData = CombinedData()
                val lineData = setPressureLine(it)
                if (lineData != null) {
                    combinedData.setData(lineData)
                    this.data = combinedData
                    this.invalidate()
                }
            }
        }

        return binding.root
    }

    private fun setPressureLine(items : MutableList<TodayRecord.AllData>) : LineData {
        var pressureItems = items.groupBy {
            it.record.recordDate.split(' ')[0]
        }.values.toMutableList()
        pressureItems.sortBy { it[0].record.recordDate }

        val lowLineEntries : MutableList<Entry> = mutableListOf()
        val highLineEntries : MutableList<Entry> = mutableListOf()

        for(item in pressureItems) {
            val localDate = LocalDate.parse(item[0].record.recordDate.split(' ')[0])
            var x = (viewModel.kindEndDate.value!!.second.until(
                localDate,
                ChronoUnit.DAYS
            )).toFloat() + 8

            highLineEntries.add(Entry(x, item.maxOf { it.record.maxPressure!! }.toFloat()))
            lowLineEntries.add(Entry(x, item.minOf { it.record.minPressure!! }.toFloat()))
        }

        return LineData(buildList {
            add(LineDataSet(highLineEntries, null).apply {
                setDrawIcons(false)
                color = ResourcesCompat.getColor(resources, R.color.red_800, null)
                setCircleColor(ResourcesCompat.getColor(resources, R.color.red_800, null))
                circleSize = 5f

                lineWidth = 2f

                setDrawCircleHole(false)

                valueTextSize = 16f
            })

            add(LineDataSet(lowLineEntries, null).apply {
                setDrawIcons(false)
                color = ResourcesCompat.getColor(resources, R.color.yellow_800, null)
                setCircleColor(ResourcesCompat.getColor(resources, R.color.yellow_800, null))
                circleSize = 5f

                lineWidth = 2f

                setDrawCircleHole(false)

                valueTextSize = 16f
            })
        })
    }
}