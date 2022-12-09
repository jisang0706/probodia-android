package com.piri.probodia.view.fragment.record.analysis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.databinding.FragmentRecordAnalysisBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.RecordAnalysisViewModel
import com.piri.probodia.widget.utils.BottomSearchFood
import java.time.LocalDate

class RecordAnalysisFragment : Fragment() {

    private lateinit var binding : FragmentRecordAnalysisBinding
    private lateinit var viewModel : RecordAnalysisViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordAnalysisBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(RecordAnalysisViewModel::class.java)
        binding.vm = viewModel

        initAnalysisGlucoseRageFragment()
        initAnalysisMealRangeFragment()
        initAnalysisGlucoseLineFragment()
        initAnalysisPressureChartFragment()
        initAnalysisRecordedRangeFragment()
        initAnalysisHemoglobinFragment()

        binding.recordAnalysisLayout.setPadding(0, 0, 0, BottomSearchFood.getBottomPadding())

        viewModel.kindEndDate.observe(viewLifecycleOwner) {
            loadAnalysis(it)
        }

        viewModel.glucoseResult.observe(viewLifecycleOwner) {
            viewModel.setGlucoseRange(it)
            binding.countText.text = "${it.size}회"
        }

        return binding.root
    }

    fun initAnalysisGlucoseRageFragment() {
        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = AnalysisGlucoseRangeFragment()
        transaction.replace(R.id.glucose_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun initAnalysisMealRangeFragment() {
        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = AnalysisMealRangeFragment()
        transaction.replace(R.id.nutrient_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun initAnalysisGlucoseLineFragment() {
        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = AnalysisGlucoseChartFragment()
        transaction.replace(R.id.glucose_line_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun initAnalysisPressureChartFragment() {
        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = AnalysisPressureChartFragment()
        transaction.replace(R.id.pressure_chart_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun initAnalysisRecordedRangeFragment() {
        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = AnalysisRecordedFragment()
        transaction.replace(R.id.recorded_range_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun initAnalysisHemoglobinFragment() {
        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = AnalysisHemoglobinFragment()
        transaction.replace(R.id.hemoglobin_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun loadAnalysis(kindDate : Pair<Int, LocalDate>) {
        viewModel.getGlucose(PreferenceRepository(
            requireContext()),
            kindDate.first,
            kindDate.second
        )

        viewModel.getPressure(PreferenceRepository(
            requireContext()),
            kindDate.first,
            kindDate.second
        )

        viewModel.getMealRange(
            PreferenceRepository(requireContext()),
            kindDate.first,
            kindDate.second
        )

        viewModel.getHemoglobin(
            PreferenceRepository(requireContext()),
            kindDate.first,
            kindDate.second
        )
    }

    fun reloadAnalysis() {
        if (::viewModel.isInitialized) {
            viewModel.kindEndDate.value?.let { loadAnalysis(it) }
        }
    }
}