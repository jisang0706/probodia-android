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
    private lateinit var recordFragment : RecordFragment
    private lateinit var challengeFragment : ChallengeFragment
    private lateinit var communityFragment : CommunityFragment
    private lateinit var etcFragment : EtcFragment

    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                if (!this::recordFragment.isInitialized) {
                    recordFragment = RecordFragment()
                }
                recordFragment
            }
            1 -> {
                if (!this::challengeFragment.isInitialized) {
                    challengeFragment = ChallengeFragment()
                }
                challengeFragment
            }
            2 -> {
                if (!this::communityFragment.isInitialized) {
                    communityFragment = CommunityFragment()
                }
                communityFragment
            }
            3 -> {
                if (!this::etcFragment.isInitialized) {
                    etcFragment = EtcFragment()
                }
                etcFragment
            }
            else -> {
                if (!this::recordFragment.isInitialized) {
                    recordFragment = RecordFragment()
                }
                recordFragment
            }
        }
    }

}