package com.piri.probodia.view.fragment.record.analysis

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.TodayRecord
import com.piri.probodia.databinding.FragmentAnalysisLineBinding
import com.piri.probodia.viewmodel.AnalysisTimeSelectorViewModel
import com.piri.probodia.viewmodel.RecordAnalysisViewModel
import com.piri.probodia.widget.utils.Convert
import com.piri.probodia.widget.utils.ScatterShape
import java.lang.Float.max
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class AnalysisGlucoseChartFragment : AnalysisBaseLineFragment() {

    private lateinit var selectorViewModel : AnalysisTimeSelectorViewModel

    private lateinit var scatterColor : List<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalysisLineBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(requireParentFragment()).get(RecordAnalysisViewModel::class.java)
        binding.vm = viewModel

        selectorViewModel = ViewModelProvider(this).get(AnalysisTimeSelectorViewModel::class.java)
        binding.selectorVm = selectorViewModel

        binding.pressureColorTextLayout.visibility = View.GONE

        initChart()

        var combinedData = CombinedData()

        scatterColor = buildList {
            add(ResourcesCompat.getColor(resources, R.color.red_800, null))
            add(ResourcesCompat.getColor(resources, R.color.orange_800, null))
            add(ResourcesCompat.getColor(resources, R.color.yellow_800, null))
            add(ResourcesCompat.getColor(resources, R.color.green_800, null))
            add(ResourcesCompat.getColor(resources, R.color.blue_800, null))
            add(ResourcesCompat.getColor(resources, R.color.black, null))
        }

        viewModel.glucoseResult.observe(viewLifecycleOwner) {
            it.sortBy { it.record.recordDate }
            binding.analysisChart.apply {
                combinedData = CombinedData()
                combinedData.setData(setGlucoseScatter(it))
                this.data = combinedData
                this.invalidate()
                selectorViewModel.setSelectedTimeTag(0)
                binding.selectFirLayout.visibility = View.VISIBLE
            }
        }

        selectorViewModel.selectedTimeTag.observe(viewLifecycleOwner) {
            binding.apply {
                val buttons = mutableListOf(
                    morningBtn,
                    lunchBtn,
                    dinnerBtn
                )

                for(i in 0 until buttons.size) {
                    if (it == i) {
                        buttons[i].setBackgroundResource(R.drawable.primary_100_1_background)
                        buttons[i].setTextColor(Color.WHITE)
                    } else {
                        buttons[i].setBackgroundResource(R.drawable.white_1_background)
                        buttons[i].setTextColor(Color.BLACK)
                    }
                }
            }

            if (viewModel.glucoseResult.value != null) {
                val glucoseList = viewModel.glucoseResult.value!!
                glucoseList.sortBy { it.record.recordDate }
                binding.analysisChart.apply {
                    val applyCombineData = CombinedData()
                    applyCombineData.setData(combinedData.scatterData)
                    applyCombineData.setData(viewModel.glucoseResult.value?.let { it1 ->
                        setGlucoseLine(
                            it1, it
                        )
                    })
                    this.data = applyCombineData
                    this.invalidate()
                }
            }
        }

        return binding.root
    }

    fun setGlucoseLine(items : MutableList<TodayRecord.AllData>, selected : Int) : LineData {
        val firLineEntries = buildList<Entry> {
            for(item in items) {
                if (item.record.timeTag == Convert.getTimeTag(selected * 2)) {
                    val localdate = LocalDate.parse(item.record.recordDate.split(' ')[0])
                    var x = (viewModel.kindEndDate.value!!.second.until(
                        localdate,
                        ChronoUnit.DAYS
                    )).toFloat() + 8

                    add(Entry(x, item.record.glucose!!.toFloat()))
                }
            }
        }

        val secLineEntries = buildList<Entry> {
            for(item in items) {
                if (item.record.timeTag == Convert.getTimeTag(selected * 2 + 1)) {
                    val localdate = LocalDate.parse(item.record.recordDate.split(' ')[0])
                    var x = (viewModel.kindEndDate.value!!.second.until(
                        localdate,
                        ChronoUnit.DAYS
                    )).toFloat() + 8

                    add(Entry(x, item.record.glucose!!.toFloat()))
                }
            }
        }

        return LineData(buildList {
            add(LineDataSet(firLineEntries, null).apply {
                setDrawIcons(false)
                color = scatterColor[selected * 2]
                setCircleColor(scatterColor[selected * 2])
                circleSize = 5f

                lineWidth = 2f

                setDrawCircleHole(false)

                valueTextSize = 16f
            })

            add(LineDataSet(secLineEntries, null).apply {
                setDrawIcons(false)
                color = scatterColor[selected * 2 + 1]
                setCircleColor(scatterColor[selected * 2 + 1])
                circleSize = 5f

                lineWidth = 2f

                setDrawCircleHole(false)

                valueTextSize = 16f
            })
        })
    }

    fun setGlucoseScatter(items : MutableList<TodayRecord.AllData>) : ScatterData {
        val scatterEntriesList = buildList {
            for(i in 0 until 6) {
                add(ArrayList<Entry>())
            }
        }

        var top = 0f
        for(item in items) {
            val localdate = LocalDate.parse(item.record.recordDate.split(' ')[0])
            var x = (viewModel.kindEndDate.value!!.second.until(localdate, ChronoUnit.DAYS)).toFloat() + 8
            scatterEntriesList[Convert.timeTagToInt(item.record.timeTag)].add(BarEntry(x, item.record.glucose!!.toFloat()))
            top = max(top, item.record.glucose!!.toFloat())
        }
        binding.apply {
            analysisChart.axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = max(200f, top + 10)
            }
        }
        val temp = ScatterData(buildList<IScatterDataSet> {
            for(i in 0 until scatterEntriesList.size) {
                add(ScatterDataSet(scatterEntriesList[i], "").apply {
                    axisDependency = YAxis.AxisDependency.LEFT
                    highLightColor = Color.TRANSPARENT
                    setScatterShape(ScatterShape.scatterShape[0])
                    color = scatterColor[i]
                    valueTextColor = Color.TRANSPARENT
                    scatterShapeSize = 30f
                })
            }
        })

        return temp
    }
}