package com.piri.probodia.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.ChallengeDto
import com.piri.probodia.databinding.ActivityChallengeParticipateBinding
import com.piri.probodia.viewmodel.ChallengeParticipateViewModel

class ChallengeParticipateActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChallengeParticipateBinding
    private lateinit var viewModel : ChallengeParticipateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_participate)

        viewModel = ViewModelProvider(this).get(ChallengeParticipateViewModel::class.java)
        binding.vm = viewModel

        binding.challengeImage.clipToOutline = true

        intent.getParcelableExtra<ChallengeDto>("DATA")?.let { viewModel.setData(it) }

        viewModel.data.observe(this) {
            binding.challengeImage.setImageResource(
                when (it.type) {
                    "혈당기록" -> R.drawable.challenge_glucose
                    "음식기록" -> R.drawable.challenge_meal
                    else -> R.drawable.challenge_image
                }
            )
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }
}