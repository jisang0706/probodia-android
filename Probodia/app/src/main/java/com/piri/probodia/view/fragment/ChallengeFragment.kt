package com.piri.probodia.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.databinding.FragmentChallengeBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.ChallengeViewModel

class ChallengeFragment : Fragment() {

    private lateinit var binding : FragmentChallengeBinding
    private lateinit var viewModel : ChallengeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChallengeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(ChallengeViewModel::class.java)
        binding.vm = viewModel

        viewModel.getChallengeList(PreferenceRepository(requireContext()))

        viewModel.result.observe(viewLifecycleOwner) {
            Log.e("CHALLENGE", it.toString())
        }

        return binding.root
    }
}