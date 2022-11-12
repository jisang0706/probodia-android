package com.piri.probodia.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.ChallengeDto
import com.piri.probodia.databinding.ActivityChallengeRecordInfoBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.ChallengeRecordInfoViewModel

class ChallengeRecordInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChallengeRecordInfoBinding
    private lateinit var viewModel : ChallengeRecordInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_record_info)

        viewModel = ViewModelProvider(this).get(ChallengeRecordInfoViewModel::class.java)
        binding.vm = viewModel

        intent.getParcelableExtra<ChallengeDto>("DATA")?.let { viewModel.setChallengeInfo(it) }

        viewModel.challengeInfo.observe(this) {
            viewModel.getChallengeRecord(PreferenceRepository(applicationContext), it.id)
        }

        viewModel.challengeDegree.observe(this) {
            binding.degreeText.text = getString(R.string.challenge_degree, it)
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }
}