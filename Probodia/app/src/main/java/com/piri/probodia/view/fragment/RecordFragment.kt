package com.piri.probodia.view.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.piri.probodia.R
import com.piri.probodia.adapter.RecordPagerAdapter
import com.piri.probodia.databinding.FragmentRecordBinding
import com.piri.probodia.viewmodel.RecordViewModel

class RecordFragment : Fragment() {

    private lateinit var binding : FragmentRecordBinding
    private lateinit var viewModel : RecordViewModel
    private lateinit var recordPagerAdapter: RecordPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)

        viewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        binding.vm = viewModel

        recordPagerAdapter = RecordPagerAdapter(childFragmentManager, lifecycle)
        recordPagerAdapter.recordTodayFragment.setReload(::reloadRecord)
        recordPagerAdapter.recordPastFragment.setReload(::reloadRecord)
        binding.recordViewpager.adapter = recordPagerAdapter
        binding.recordViewpager.isUserInputEnabled = false
        binding.recordViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0 || position == 1) {
                    binding.introText.visibility = View.VISIBLE
                    binding.recordBtnLayout.visibility = View.VISIBLE
                } else {
                    binding.introText.visibility = View.GONE
                    binding.recordBtnLayout.visibility = View.GONE
                }
            }
        })

        binding.recordTabs.setSelectedTabIndicatorColor(Color.BLACK)
        TabLayoutMediator(binding.recordTabs, binding.recordViewpager) { tab, position ->
            tab.text = recordPagerAdapter.getItemTitle(position)
        }.attach()

        binding.recordGlucoseLayout.setOnClickListener {
            binding.recordGlucoseBtn.callOnClick()
        }

        binding.recordGlucoseBtn.setOnClickListener {
            val fragment = RecordGlucoseFragment(::reloadRecord, 0, null)
            fragment.show(childFragmentManager, fragment.tag)
        }

        binding.recordPressureLayout.setOnClickListener {
            binding.recordPressureBtn.callOnClick()
        }

        binding.recordPressureBtn.setOnClickListener {
            val fragment = RecordPressureFragment(::reloadRecord, 0, null)
            fragment.show(childFragmentManager, fragment.tag)
        }

        binding.recordMedicineLayout.setOnClickListener {
            binding.recordMedicineBtn.callOnClick()
        }

        binding.recordMedicineBtn.setOnClickListener {
            val fragment = RecordMedicineFragment(::reloadRecord, 0, null)
            fragment.show(childFragmentManager, fragment.tag)
        }

        binding.recordMealLayout.setOnClickListener {
            binding.recordMealBtn.callOnClick()
        }

        binding.recordMealBtn.setOnClickListener {
            val fragment = RecordMealFragment(::reloadRecord, 0, null)
            fragment.show(childFragmentManager, fragment.tag)
        }

        return binding.root
    }

    fun reloadRecord() {
        recordPagerAdapter.recordTodayFragment.loadTodayRecord()
        recordPagerAdapter.recordPastFragment.restartRecord()
        recordPagerAdapter.recordAnalysisFragment.reloadAnalysis()
    }
}