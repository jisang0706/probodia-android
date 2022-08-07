package com.example.probodia.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.probodia.R
import com.example.probodia.adapter.RecordPagerAdapter
import com.example.probodia.databinding.FragmentRecordBinding
import com.example.probodia.viewmodel.RecordViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RecordFragment : Fragment() {

    private lateinit var binding : FragmentRecordBinding
    private lateinit var viewModel : RecordViewModel
    private lateinit var recordPagerAdapter: RecordPagerAdapter

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

        return binding.root
    }
}