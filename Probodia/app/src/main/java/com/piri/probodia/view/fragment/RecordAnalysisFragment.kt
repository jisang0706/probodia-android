package com.piri.probodia.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.piri.probodia.R
import com.piri.probodia.databinding.FragmentRecordAnalysisBinding

class RecordAnalysisFragment : Fragment() {

    private lateinit var binding : FragmentRecordAnalysisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordAnalysisBinding.inflate(layoutInflater)

        initAnalysisGlucoseRageFragment()

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