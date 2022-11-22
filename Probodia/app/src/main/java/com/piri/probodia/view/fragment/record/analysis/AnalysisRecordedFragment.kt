package com.piri.probodia.view.fragment.record.analysis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.databinding.FragmentAnalysisRecordedBinding
import com.piri.probodia.viewmodel.AnalysisRecordedViewModel
import com.piri.probodia.viewmodel.RecordAnalysisViewModel

class AnalysisRecordedFragment : Fragment() {

    private lateinit var binding : FragmentAnalysisRecordedBinding

    private lateinit var viewModel : RecordAnalysisViewModel
    private lateinit var recordedViewModel : AnalysisRecordedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalysisRecordedBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(requireParentFragment()).get(RecordAnalysisViewModel::class.java)
        binding.vm = viewModel

        recordedViewModel = ViewModelProvider(this).get(AnalysisRecordedViewModel::class.java)
        binding.recordedVm = recordedViewModel

        initAnalysisFirstRangeFragment()
        initAnalysisSecondRangeFragment()
        initAnalysisThirdRangeFragment()

        viewModel.glucoseResult.observe(viewLifecycleOwner) {
            recordedViewModel.setRecordedRange(it)
        }

        return binding.root
    }

    fun initAnalysisFirstRangeFragment() {
        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = AnalysisRecordedFirstFragment()
        transaction.replace(R.id.fir_range_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun initAnalysisSecondRangeFragment() {
        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = AnalysisRecordedSecondFragment()
        transaction.replace(R.id.sec_range_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun initAnalysisThirdRangeFragment() {
        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = AnalysisRecordedThirdFragment()
        transaction.replace(R.id.thr_range_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}