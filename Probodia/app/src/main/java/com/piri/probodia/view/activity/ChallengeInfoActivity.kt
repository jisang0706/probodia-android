package com.piri.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.ChallengeDto
import com.piri.probodia.databinding.ActivityChallengeInfoBinding
import com.piri.probodia.view.fragment.challenge.ChallengeFragment
import com.piri.probodia.viewmodel.ChallengeInfoViewModel

class ChallengeInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChallengeInfoBinding
    private lateinit var viewModel : ChallengeInfoViewModel

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_info)

        viewModel = ViewModelProvider(this).get(ChallengeInfoViewModel::class.java)
        binding.vm = viewModel

        viewModel.setData(intent.getParcelableExtra("DATA")!!)

        setEnterBtnPosition()

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

        binding.enterBtn.setOnClickListener {
            val intent = Intent(applicationContext, ChallengeParticipateActivity::class.java)
            intent.putExtra("DATA", viewModel.data.value)
            activityResultLauncher.launch(intent)
        }

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result : ActivityResult ->
            val resultIntent = result.data
            if (resultIntent != null) {
                if (result.resultCode == R.integer.challenge_participant_code) {
                    val returnIntent = Intent(applicationContext, ChallengeFragment::class.java)
                    setResult(R.integer.challenge_participant_code, returnIntent)
                    finish()
                }
            }
        }
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