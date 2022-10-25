package com.piri.probodia.view.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.piri.probodia.R
import com.piri.probodia.adapter.ShowDoctorPagerAdapter
import com.piri.probodia.databinding.ActivityShowDoctorBinding

class ShowDoctorActivity : AppCompatActivity() {

    private lateinit var binding : ActivityShowDoctorBinding
    private lateinit var showPagerAdapter : ShowDoctorPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_doctor)
        binding.lifecycleOwner = this

        showPagerAdapter = ShowDoctorPagerAdapter(supportFragmentManager, lifecycle)
        binding.showViewpager.adapter = showPagerAdapter
        binding.showViewpager.isUserInputEnabled = false

        binding.showTabs.setSelectedTabIndicatorColor(Color.BLACK)
        TabLayoutMediator(binding.showTabs, binding.showViewpager) { tab, position ->
            tab.text = showPagerAdapter.getItemTitle(position)
        }.attach()

        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }
}