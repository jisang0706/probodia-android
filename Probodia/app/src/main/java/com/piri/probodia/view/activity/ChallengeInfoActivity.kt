package com.piri.probodia.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.ChallengeDto
import com.piri.probodia.databinding.ActivityChallengeInfoBinding
import com.piri.probodia.viewmodel.ChallengeInfoViewModel

class ChallengeInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChallengeInfoBinding
    private lateinit var viewModel : ChallengeInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_info)

        viewModel = ViewModelProvider(this).get(ChallengeInfoViewModel::class.java)
        binding.vm = viewModel

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        viewModel.setData(intent.getParcelableExtra("DATA")!!)
    }
}