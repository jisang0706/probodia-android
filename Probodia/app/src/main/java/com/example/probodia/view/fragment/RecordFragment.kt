package com.example.probodia.view.fragment

import android.content.Intent
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
import com.example.probodia.R
import com.example.probodia.adapter.RecordPagerAdapter
import com.example.probodia.databinding.FragmentRecordBinding
import com.example.probodia.view.activity.RecordGlucoseActivity
import com.example.probodia.view.activity.RecordPressureActivity
import com.example.probodia.viewmodel.RecordViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RecordFragment : Fragment() {

    private lateinit var binding : FragmentRecordBinding
    private lateinit var viewModel : RecordViewModel
    private lateinit var recordPagerAdapter: RecordPagerAdapter
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)

        viewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        binding.vm = viewModel

        recordPagerAdapter = RecordPagerAdapter(childFragmentManager, lifecycle)
        binding.recordViewpager.adapter = recordPagerAdapter
        binding.recordViewpager.isUserInputEnabled = false
        binding.recordViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    binding.introText.visibility = View.VISIBLE
                    binding.recordBtnLayout.visibility = View.VISIBLE
                } else {
                    binding.introText.visibility = View.GONE
                    binding.recordBtnLayout.visibility = View.GONE
                }
            }
        })

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
                        recordPagerAdapter.recordTodayFragment.reloadTodayRecord()
                    }
                }
            }
        }

        binding.recordGlucoseBtn.setOnClickListener {
            val intent = Intent(activity, RecordGlucoseActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        binding.recordPressureBtn.setOnClickListener {
            val intent = Intent(activity, RecordPressureActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        return binding.root
    }
}