package com.piri.probodia.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.piri.probodia.view.fragment.etc.ShowDoctorGlucoseFragment
import com.piri.probodia.view.fragment.etc.ShowDoctorPressureFragment

class ShowDoctorPagerAdapter(fm : FragmentManager, lc : Lifecycle) : FragmentStateAdapter(fm, lc) {
    private val showDoctorGlucoseFragment = ShowDoctorGlucoseFragment()
    private val showDoctorPressureFragment = ShowDoctorPressureFragment()

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> showDoctorGlucoseFragment
            1 -> showDoctorPressureFragment
            else -> showDoctorGlucoseFragment
        }
    }

    fun getItemTitle(position : Int) : String {
        return when(position) {
            0 -> "혈당"
            1 -> "혈압"
            else -> "혈당"
        }
    }
}