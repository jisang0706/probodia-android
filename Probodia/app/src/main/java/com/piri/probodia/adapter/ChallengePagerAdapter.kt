package com.piri.probodia.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.piri.probodia.view.fragment.challenge.ChallengeHistoryFragment
import com.piri.probodia.view.fragment.challenge.ChallengeParticipatingFragment
import com.piri.probodia.view.fragment.challenge.ChallengeViewFragment

class ChallengePagerAdapter(fm : FragmentManager, lc : Lifecycle) : FragmentStateAdapter(fm, lc) {
    val challengeViewFragment = ChallengeViewFragment(::refreshFragment)
    val challengeParticipatingFragment = ChallengeParticipatingFragment()
    val challengeHistoryFragment = ChallengeHistoryFragment()

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> challengeViewFragment
            1 -> challengeParticipatingFragment
            2 -> challengeHistoryFragment
            else -> challengeViewFragment
        }
    }

    fun getItemTitle(position : Int) : String {
        return when(position) {
            0 -> "구경하기"
            1 -> "참가중인 챌린지"
            2 -> "내 기록"
            else -> "구경하기"
        }
    }

    fun refreshFragment() {
        challengeViewFragment.getChallengeList()
        challengeParticipatingFragment.getParticipatingChallengeList()
    }
}