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
import com.piri.probodia.adapter.ChallengeViewAdapter
import com.piri.probodia.databinding.FragmentChallengeViewBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.view.activity.ChallengeInfoActivity
import com.piri.probodia.viewmodel.ChallengeViewViewModel

class ChallengeViewFragment : Fragment() {

    private lateinit var binding : FragmentChallengeViewBinding
    private lateinit var viewModel : ChallengeViewViewModel
    private lateinit var challengeViewAdapter : ChallengeViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChallengeViewBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(ChallengeViewViewModel::class.java)
        binding.vm = viewModel

        challengeViewAdapter = ChallengeViewAdapter()

        binding.challengeRv.adapter = challengeViewAdapter
        binding.challengeRv.layoutManager = LinearLayoutManager(context)

        challengeViewAdapter.setOnItemClickListener(object : ChallengeViewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(), ChallengeInfoActivity::class.java)
                intent.putExtra("DATA", challengeViewAdapter.getData(position))
                startActivity(intent)
            }
        })

        viewModel.getChallengeList(PreferenceRepository(requireContext()))

        viewModel.challengeResult.observe(viewLifecycleOwner) {
            challengeViewAdapter.setData(it)
            challengeViewAdapter.notifyDataSetChanged()
        }

        return binding.root
    }
}