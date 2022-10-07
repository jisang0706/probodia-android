package com.piri.probodia.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.databinding.FragmentRecordAnalysisBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.RecordAnalisysViewModel

class RecordAnalysisFragment : Fragment() {

    private lateinit var binding : FragmentRecordAnalysisBinding
    private lateinit var viewModel : RecordAnalisysViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordAnalysisBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(RecordAnalisysViewModel::class.java)
        binding.vm = viewModel

        initAnalysisGlucoseRageFragment()

        viewModel.kindEndDate.observe(viewLifecycleOwner) {
            viewModel.getGlucose(PreferenceRepository(
                requireContext()),
                it.first,
                it.second
            )
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
}