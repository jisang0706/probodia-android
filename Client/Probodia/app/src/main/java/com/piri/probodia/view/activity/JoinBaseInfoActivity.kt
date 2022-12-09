package com.piri.probodia.view.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.databinding.ActivityJoinBaseInfoBinding
import com.piri.probodia.view.fragment.etc.EtcFragment
import com.piri.probodia.viewmodel.JoinBaseInfoViewModel

class JoinBaseInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinBaseInfoBinding
    private lateinit var viewModel : JoinBaseInfoViewModel
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    private var isEdit : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join_base_info)

        viewModel = ViewModelProvider(this).get(JoinBaseInfoViewModel::class.java)
        binding.vm = viewModel

        binding.layoutBirthEdit.titleText.text = "나이"
        binding.layoutBirthEdit.edit.hint = "나이 입력 (ex. 45)"

        viewModel.setSelectedTitle("성별")
        binding.genderSelectLayout.firBtn.text = "여성"
        binding.genderSelectLayout.secBtn.text = "남성"
        binding.genderSelectLayout.thrBtn.visibility = View.GONE

        isEdit = intent.getBooleanExtra("ISEDIT", false)

        binding.layoutBirthEdit.edit.addTextChangedListener {
            viewModel.setButtonClickEnable(it?.length!! > 0)
        }

        viewModel.buttonClickEnable.observe(this) {
            if (it) {
                binding.enterBtn.setBackgroundResource(R.drawable.primary_100_2_background)
            } else {
                binding.enterBtn.setBackgroundResource(R.drawable.gray_300_2_background)
            }
        }

        viewModel.selectedButton.observe(this) {
            val btnList = listOf(binding.genderSelectLayout.firBtn,
                binding.genderSelectLayout.secBtn)

            for(btn in btnList) {
                btn.setBackgroundResource(R.drawable.white_2_background)
                btn.setTextColor(Color.BLACK)
            }

            btnList[it - 1].setBackgroundResource(R.drawable.primary_100_2_background)
            btnList[it - 1].setTextColor(Color.WHITE)
        }

        binding.enterBtn.setOnClickListener {
            if (viewModel.buttonClickEnable.value!!) {
                val goIntent = Intent(applicationContext, JoinLastInfoActivity::class.java)
                goIntent.putExtra("AGE", binding.layoutBirthEdit.edit.text.toString().toInt())
                val sex : () -> String = { if (viewModel.selectedButton.value == 1) {"W"} else {"M"} }
                goIntent.putExtra("SEX", sex())
                goIntent.putExtra("USERID", intent.getLongExtra("USERID", 0))
                goIntent.putExtra("KAKAOACCESS", intent.getStringExtra("KAKAOACCESS"))
                goIntent.putExtra("ISEDIT", isEdit)
                if (isEdit) {
                    goIntent.putExtra("HEIGHT", intent.getIntExtra("HEIGHT", 0))
                    goIntent.putExtra("WEIGHT", intent.getIntExtra("WEIGHT", 0))
                    goIntent.putExtra("DIABETE", intent.getStringExtra("DIABETE"))
                }
                activityResultLauncher.launch(goIntent)
            } else {
                Toast.makeText(applicationContext, "나이를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        if (isEdit) {
            binding.layoutBirthEdit.edit.setText("${intent.getIntExtra("AGE", 0)}")
            viewModel.setButtonClickEnable(binding.layoutBirthEdit.edit.text.length!! > 0)
            if (intent.getStringExtra("SEX") == "M") {
                viewModel.setSelectedButton(2)
            }
        }

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result : ActivityResult ->
            val resultIntent = result.data
            if (resultIntent != null) {
                if (result.resultCode == R.integer.join_success) {
                    if (isEdit) {
                        val resultIntent = Intent(applicationContext, EtcFragment::class.java)
                        setResult(R.integer.join_success, resultIntent)
                    }
                    finish()
                }
            }
        }
    }
}