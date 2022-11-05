package com.piri.probodia.view.fragment.challenge

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.piri.probodia.adapter.ChallengePagerAdapter
import com.piri.probodia.databinding.FragmentChallengeBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.ChallengeViewModel

class ChallengeFragment : Fragment() {

    private lateinit var binding : FragmentChallengeBinding
    private lateinit var viewModel : ChallengeViewModel
    private lateinit var challengePagerAdapter : ChallengePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChallengeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(ChallengeViewModel::class.java)
        binding.vm = viewModel

        challengePagerAdapter = ChallengePagerAdapter(childFragmentManager, lifecycle)

        binding.challengeViewpager.adapter = challengePagerAdapter
        binding.challengeViewpager.isUserInputEnabled = false

        binding.challengeTabs.setSelectedTabIndicatorColor(Color.BLACK)
        TabLayoutMediator(binding.challengeTabs, binding.challengeViewpager) { tab, position ->
            tab.text = challengePagerAdapter.getItemTitle(position)
        }.attach()

        return binding.root
    }
}