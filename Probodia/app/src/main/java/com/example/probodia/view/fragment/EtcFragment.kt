package com.example.probodia.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.databinding.FragmentEtcBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.viewmodel.EtcViewModel

class EtcFragment : Fragment() {

    private lateinit var binding : FragmentEtcBinding
    private lateinit var viewModel : EtcViewModel
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_etc, container, false)

        viewModel = ViewModelProvider(this).get(EtcViewModel::class.java)
        binding.vm = viewModel

        viewModel.getUserId()

        viewModel.userId.observe(requireActivity(), {
            viewModel.getUserProfile(PreferenceRepository(requireContext()), it)
        })

        viewModel.userData.observe(requireActivity()) {
            binding.userAgeText.text = "${it.age}세"
            binding.userBodyText.text = "${it.height}cm / ${it.weight}kg"
        }

        return binding.root
    }
}