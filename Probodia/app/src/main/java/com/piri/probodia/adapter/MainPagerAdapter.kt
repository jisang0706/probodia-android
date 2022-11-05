package com.piri.probodia.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.piri.probodia.view.fragment.challenge.ChallengeFragment
import com.piri.probodia.view.fragment.community.CommunityFragment
import com.piri.probodia.view.fragment.etc.EtcFragment
import com.piri.probodia.view.fragment.record.RecordFragment

class MainPagerAdapter(fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {
    val recordFragment = RecordFragment()
    val challengeFragment = ChallengeFragment()
    val communityFragment = CommunityFragment()
    val etcFragment = EtcFragment()

    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> recordFragment
            1 -> challengeFragment
            2 -> communityFragment
            3 -> etcFragment
            else -> recordFragment
        }
    }

}