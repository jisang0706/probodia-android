package com.piri.probodia.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
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

        setEnterBtnPosition()
    }

    private fun setEnterBtnPosition() {
        if (binding.challengeInfoBaseLayout.height != 0) {
            _setEnterBtnPosition()
        } else {
            binding.challengeInfoBaseLayout.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        binding.challengeInfoBaseLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        _setEnterBtnPosition()
                    }
                }
            )
        }
    }

    private fun _setEnterBtnPosition() {
        if (binding.challengeInfoBaseLayout.height < binding.root.height) {
            val params = ViewGroup.MarginLayoutParams(binding.enterBtn.layoutParams)
            params.setMargins(
                20,
                binding.root.height - binding.challengeInfoBaseLayout.height + 16,
                20,
                16
            )
            binding.enterBtn.layoutParams = LinearLayout.LayoutParams(params)
        }
    }
}