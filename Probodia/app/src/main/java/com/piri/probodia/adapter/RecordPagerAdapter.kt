package com.piri.probodia.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.piri.probodia.data.remote.model.SortationDto
import com.piri.probodia.view.fragment.record.analysis.RecordAnalysisFragment
import com.piri.probodia.view.fragment.record.RecordPastFragment
import com.piri.probodia.view.fragment.record.RecordTodayFragment

class RecordPagerAdapter(fm: FragmentManager, lc: Lifecycle, record : (sortation : SortationDto, kind : Int) -> Unit) : FragmentStateAdapter(fm, lc) {
    val recordTodayFragment = RecordTodayFragment(record)
    val recordPastFragment = RecordPastFragment(record)
    val recordAnalysisFragment = RecordAnalysisFragment()

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> recordTodayFragment
            1 -> recordPastFragment
            2 -> recordAnalysisFragment
            else -> recordTodayFragment
        }
    }

    fun getItemTitle(position: Int): String {
        return when(position) {
            0 -> "오늘"
            1 -> "과거"
            2 -> "분석 리포트"
            else -> "오늘"
        }
    }
}