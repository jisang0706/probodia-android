package com.example.probodia.view.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.databinding.ActivityJoinLastInfoBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.viewmodel.JoinLastInfoViewModel

class JoinLastInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinLastInfoBinding
    private lateinit var viewModel : JoinLastInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join_last_info)

        viewModel = ViewModelProvider(this).get(JoinLastInfoViewModel::class.java)
        binding.vm = viewModel

        binding.heightLayout.titleText.text = "키"
        binding.heightLayout.edit.hint = "cm 단위로 입력 (ex. 168)"
        binding.weightLayout.titleText.text = "몸무게"
        binding.weightLayout.edit.hint = "kg 단위로 입력 (ex. 52)"

        viewModel.setSelectedTitle("당뇨 유형")
        binding.diabeteSelectLayout.firBtn.text = "1형 당뇨"
        binding.diabeteSelectLayout.secBtn.text = "2형 당뇨"
        binding.diabeteSelectLayout.thrBtn.text = "임신성 당뇨"

        binding.heightLayout.edit.addTextChangedListener {
            viewModel.setButtonClickEnable(
                binding.heightLayout.edit.text.isNotEmpty() &&
                        binding.weightLayout.edit.text.isNotEmpty()
            )
        }

        binding.weightLayout.edit.addTextChangedListener {
            viewModel.setButtonClickEnable(
                binding.heightLayout.edit.text.isNotEmpty() &&
                        binding.weightLayout.edit.text.isNotEmpty()
            )
        }

        viewModel.buttonClickEnable.observe(this) {
            if (it) {
                binding.enterBtn.setBackgroundResource(R.drawable.primary_100_2_background)
            } else {
                binding.enterBtn.setBackgroundResource(R.drawable.gray_300_2_background)
            }
        }

        viewModel.selectedButton.observe(this) {
            val btnList = listOf(binding.diabeteSelectLayout.firBtn,
                binding.diabeteSelectLayout.secBtn, binding.diabeteSelectLayout.thrBtn)

            for(btn in btnList) {
                btn.setBackgroundResource(R.drawable.white_2_background)
                btn.setTextColor(Color.BLACK)
            }

            btnList[it - 1].setBackgroundResource(R.drawable.primary_100_2_background)
            btnList[it - 1].setTextColor(Color.WHITE)
        }

        binding.enterBtn.setOnClickListener {
            if (viewModel.buttonClickEnable.value!!) {
                viewModel.getUserId()
            } else {
                Toast.makeText(applicationContext, "키와 몸무게를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isError.observe(this) {
            Toast.makeText(applicationContext, "인터넷 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
        }

        viewModel.userId.observe(this) {
            viewModel.putUserData(
                PreferenceRepository(applicationContext),
                it,
                intent.getIntExtra("AGE", 0),
                intent.getStringExtra("SEX")!!,
                binding.heightLayout.edit.text.toString().toInt(),
                binding.weightLayout.edit.text.toString().toInt(),
                when(viewModel.selectedButton.value) {
                    1 -> "1형 당뇨"
                    2 -> "2형 당뇨"
                    3 -> "임신성 당뇨"
                    else -> "2형 당뇨"
                }
            )
        }

        viewModel.joinResult.observe(this) {
            val resultIntent = Intent(applicationContext, JoinBaseInfoActivity::class.java)
            setResult(R.integer.join_success, resultIntent)
            val goIntent = Intent(applicationContext, MainActivity::class.java)
            startActivity(goIntent)
            finish()
        }
    }
}