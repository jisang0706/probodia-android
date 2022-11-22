package com.piri.probodia.view.fragment.challenge

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.piri.probodia.R
import com.piri.probodia.adapter.ChallengeParticipatingAdapter
import com.piri.probodia.databinding.FragmentChallengeParticipatingBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.view.activity.ChallengeInfoActivity
import com.piri.probodia.view.activity.ChallengeRecordInfoActivity
import com.piri.probodia.viewmodel.ChallengeParticipatingViewModel

class ChallengeParticipatingFragment() : Fragment() {

    private lateinit var binding : FragmentChallengeParticipatingBinding
    private lateinit var viewModel : ChallengeParticipatingViewModel
    private lateinit var challengeParticipatingAdapter : ChallengeParticipatingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChallengeParticipatingBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(ChallengeParticipatingViewModel::class.java)
        binding.vm = viewModel

        challengeParticipatingAdapter = ChallengeParticipatingAdapter()

        binding.challengeRv.adapter = challengeParticipatingAdapter
        binding.challengeRv.layoutManager = LinearLayoutManager(context)

        challengeParticipatingAdapter.setOnItemClickListener(object : ChallengeParticipatingAdapter.OnItemClickListener {
            override fun onItemClick(position : Int) {
                val intent = Intent(requireContext(), ChallengeRecordInfoActivity::class.java)
                intent.putExtra("DATA", challengeParticipatingAdapter.getData(position))
                startActivity(intent)
            }
        })

        getParticipatingChallengeList()

        viewModel.challengeResult.observe(viewLifecycleOwner) {
            challengeParticipatingAdapter.setData(it)
            challengeParticipatingAdapter.notifyDataSetChanged()
        }

        return binding.root
    }

    fun getParticipatingChallengeList() {
        viewModel.getParticipatingChallengeList(PreferenceRepository(requireContext()))
    }
}