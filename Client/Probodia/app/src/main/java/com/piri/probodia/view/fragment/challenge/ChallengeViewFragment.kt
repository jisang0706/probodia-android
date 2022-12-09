package com.piri.probodia.view.fragment.challenge

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.piri.probodia.R
import com.piri.probodia.adapter.ChallengeViewAdapter
import com.piri.probodia.databinding.FragmentChallengeViewBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.view.activity.ChallengeInfoActivity
import com.piri.probodia.viewmodel.ChallengeViewViewModel

class ChallengeViewFragment() : Fragment() {

    private lateinit var binding : FragmentChallengeViewBinding
    private lateinit var viewModel : ChallengeViewViewModel
    private lateinit var challengeViewAdapter : ChallengeViewAdapter

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    private lateinit var refreshFragment : () -> Unit

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
                activityResultLauncher.launch(intent)
            }
        })

        getChallengeList()

        viewModel.challengeResult.observe(viewLifecycleOwner) {
            challengeViewAdapter.setData(it)
            challengeViewAdapter.notifyDataSetChanged()
        }

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result : ActivityResult ->
            val resultIntent = result.data
            if (resultIntent != null) {
                if (result.resultCode == R.integer.challenge_participant_code) {
                    if (this::refreshFragment.isInitialized) {
                        getChallengeList()
                    }
                    refreshFragment()
                }
            }
        }

        return binding.root
    }

    fun setRefresh(refresh : () -> Unit) {
        refreshFragment = refresh
    }

    fun getChallengeList() {
        viewModel.getChallengeList(PreferenceRepository(requireContext()))
    }
}