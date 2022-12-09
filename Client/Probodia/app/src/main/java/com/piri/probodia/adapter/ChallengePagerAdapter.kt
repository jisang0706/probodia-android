package com.piri.probodia.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.piri.probodia.view.fragment.challenge.ChallengeHistoryFragment
import com.piri.probodia.view.fragment.challenge.ChallengeParticipatingFragment
import com.piri.probodia.view.fragment.challenge.ChallengeViewFragment

class ChallengePagerAdapter(fm : FragmentManager, lc : Lifecycle) : FragmentStateAdapter(fm, lc) {
    private lateinit var challengeViewFragment : ChallengeViewFragment
    private lateinit var challengeParticipatingFragment : ChallengeParticipatingFragment

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                if (!this::challengeViewFragment.isInitialized) {
                    challengeViewFragment = ChallengeViewFragment()
                    challengeViewFragment.setRefresh(::refreshFragment)
                }
                challengeViewFragment
            }
            1 -> {
                if(!this::challengeParticipatingFragment.isInitialized) {
                    challengeParticipatingFragment = ChallengeParticipatingFragment()
                }
                challengeParticipatingFragment
            }
            else -> challengeViewFragment
        }
    }

    fun getItemTitle(position : Int) : String {
        return when(position) {
            0 -> "구경하기"
            1 -> "참가중인 챌린지"
            else -> "구경하기"
        }
    }

    private fun refreshFragment() {
        if (this::challengeParticipatingFragment.isInitialized) {
            challengeParticipatingFragment.getParticipatingChallengeList()
        }
    }
}