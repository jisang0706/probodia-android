package com.example.probodia.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.probodia.view.fragment.*

class MainPagerAdapter(fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {
    val recordFragment = RecordFragment()
    val challengeFragment = ChallengeFragment()
    val searchFoodFragment = SearchFoodFragment()
    val communityFragment = CommunityFragment()
    val etcFragment = EtcFragment()

    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> recordFragment
            1 -> challengeFragment
            2 -> searchFoodFragment
            3 -> communityFragment
            4 -> etcFragment
            else -> recordFragment
        }
    }

}