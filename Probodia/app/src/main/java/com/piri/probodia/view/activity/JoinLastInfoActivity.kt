package com.piri.probodia.view.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.databinding.ActivityJoinLastInfoBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.JoinLastInfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JoinLastInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinLastInfoBinding
    private lateinit var viewModel : JoinLastInfoViewModel

    private var isEdit : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join_last_info)

        viewModel = ViewModelProvider(this).get(JoinLastInfoViewModel::class.java)
        binding.vm = viewModel

        isEdit = intent.getBooleanExtra("ISEDIT", false)

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
                CoroutineScope(Dispatchers.IO).launch {
                    val userId = intent.getLongExtra("USERID", 0)
                    if (isEdit) {
                        setUserInfo(userId)
                    } else {
                        val apiToken =
                            viewModel.getApiToken(userId, intent.getStringExtra("KAKAOACCESS")!!)
                        Log.e("TOKEN", "${apiToken}")
                        viewModel.saveApiToken(PreferenceRepository(applicationContext), apiToken)
                        setUserInfo(userId)
                    }
                }
            } else {
                Toast.makeText(applicationContext, "키와 몸무게를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        if (isEdit) {
            binding.heightLayout.edit.setText("${intent.getIntExtra("HEIGHT", 0)}")
            binding.weightLayout.edit.setText("${intent.getIntExtra("WEIGHT", 0)}")
            viewModel.setButtonClickEnable(
                binding.heightLayout.edit.text.isNotEmpty() &&
                        binding.weightLayout.edit.text.isNotEmpty()
            )

            when (intent.getStringExtra("DIABETE")) {
                "1형 당뇨" -> viewModel.setSelectedButton(1)
                "2형 당뇨" -> viewModel.setSelectedButton(2)
                "임신성 당뇨" -> viewModel.setSelectedButton(3)
            }
        }

        viewModel.isError.observe(this) {
            Toast.makeText(applicationContext, "인터넷 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
        }

        viewModel.joinResult.observe(this) {
            if (isEdit) {
                val resultIntent = Intent(applicationContext, JoinBaseInfoActivity::class.java)
                setResult(R.integer.join_success, resultIntent)
                finish()
            } else {
                val resultIntent = Intent(applicationContext, JoinBaseInfoActivity::class.java)
                setResult(R.integer.join_success, resultIntent)
                val goIntent = Intent(applicationContext, MainActivity::class.java)
                startActivity(goIntent)
                finish()
            }
        }
    }

    fun setUserInfo(userId : Long) {
        viewModel.putUserData(
            PreferenceRepository(applicationContext),
            userId.toString(),
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
}