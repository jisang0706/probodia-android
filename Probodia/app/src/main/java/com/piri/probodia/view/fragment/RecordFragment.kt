package com.piri.probodia.view.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.piri.probodia.R
import com.piri.probodia.adapter.RecordPagerAdapter
import com.piri.probodia.data.remote.model.*
import com.piri.probodia.databinding.FragmentRecordBinding
import com.piri.probodia.viewmodel.RecordViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordFragment : Fragment() {

    private lateinit var binding : FragmentRecordBinding
    private lateinit var viewModel : RecordViewModel
    private lateinit var recordPagerAdapter: RecordPagerAdapter
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)

        viewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        binding.vm = viewModel

        recordPagerAdapter = RecordPagerAdapter(childFragmentManager, lifecycle, ::record)

        recordPagerAdapter.recordTodayFragment.setReload(::reloadRecord)
        recordPagerAdapter.recordPastFragment.setReload(::reloadRecord)

        binding.recordViewpager.adapter = recordPagerAdapter
        binding.recordViewpager.isUserInputEnabled = false
        binding.recordViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0 || position == 1) {
                    binding.introText.visibility = View.VISIBLE
                } else {
                    binding.introText.visibility = View.GONE
                }
            }
        })

        binding.recordTabs.setSelectedTabIndicatorColor(Color.BLACK)
        TabLayoutMediator(binding.recordTabs, binding.recordViewpager) { tab, position ->
            tab.text = recordPagerAdapter.getItemTitle(position)
        }.attach()

        activityResultLauncher = registerForActivityResult(
            StartActivityForResult()
        ) { result: ActivityResult ->
            val intent = result.data
            if (intent != null) {
                if (mutableListOf(
                        R.integer.record_glucose_result_code,
                        R.integer.record_pressure_result_code,
                        R.integer.record_medicine_result_code,
                        R.integer.record_meal_result_code
                    ).any{ it == result.resultCode}) {
                    val reload = intent!!.getBooleanExtra("RELOAD", false)
                    if (reload) {
                        reloadRecord()
                    }
                }
            }
        }

        return binding.root
    }

    fun reloadRecord() {
        recordPagerAdapter.recordTodayFragment.loadTodayRecord()
        recordPagerAdapter.recordPastFragment.restartRecord()
        recordPagerAdapter.recordAnalysisFragment.reloadAnalysis()
    }

    fun record(sortation : SortationDto, kind : Int) {
        val nowDateTime = LocalDateTime.now()
        val nowDateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val recordDate = "${sortation.record.recordDate} ${nowDateTime.format(nowDateTimeFormatter)}"
        when(kind) {
            R.integer.record_glucose -> {
                val fragment = RecordGlucoseFragment(::reloadRecord, 0,
                    GlucoseDto.Record("${sortation.record.timeTag} 식전", 0, 0, recordDate)
                )
                fragment.show(childFragmentManager, fragment.tag)
            }

            R.integer.record_pressure -> {
                val fragment = RecordPressureFragment(::reloadRecord, 0,
                    PressureDto.Record(sortation.record.timeTag, 0, 0, 0, 0, recordDate)
                )
                fragment.show(childFragmentManager, fragment.tag)
            }

            R.integer.record_medicine -> {
                val fragment = RecordMedicineFragment(::reloadRecord, 0,
                    MedicineDto.Record(sortation.record.timeTag, 0, recordDate, listOf())
                )
                fragment.show(childFragmentManager, fragment.tag)
            }

            R.integer.record_meal -> {
                val fragment = RecordMealFragment(::reloadRecord, 0,
                    MealDto.Record(sortation.record.timeTag, listOf(), 0, recordDate)
                )
                fragment.show(childFragmentManager, fragment.tag)
            }
        }
    }
}