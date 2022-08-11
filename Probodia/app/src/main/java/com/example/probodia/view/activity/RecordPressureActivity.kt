package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.databinding.ActivityRecordPressureBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.view.fragment.RecordFragment
import com.example.probodia.view.fragment.TimeSelectorFragment
import com.example.probodia.viewmodel.RecordAnythingViewModel
import com.example.probodia.viewmodel.factory.RecordAnythingViewModelFactory
import java.time.format.DateTimeFormatter

class RecordPressureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecordPressureBinding
    private lateinit var viewModel: RecordAnythingViewModel
    private lateinit var viewModelFactory : RecordAnythingViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_pressure)
        viewModelFactory = RecordAnythingViewModelFactory(PreferenceRepository(applicationContext), 2)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecordAnythingViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        initTimeSelector()

        binding.enterBtn.setOnClickListener {
            if (viewModel.buttonClickEnable.value!!) {
                viewModel.postPressure(
                    when(viewModel.selectedTimeTag.value) {
                        1 -> "아침"
                        2 -> "점심"
                        3 -> "저녁"
                        else -> "아침"
                    },
                    binding.highPressureEdit.text.toString().toInt(),
                    binding.lowPressureEdit.text.toString().toInt(),
                    binding.heartRateEdit.text.toString().toInt(),
                    viewModel.localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
            } else {
                when(0) {
                    binding.highPressureEdit.text.length ->
                        Toast.makeText(applicationContext, "입력된 최고 혈압 수치가 없습니다.", Toast.LENGTH_LONG).show()

                    binding.lowPressureEdit.text.length ->
                        Toast.makeText(applicationContext, "입력된 최저 혈압 수치가 없습니다.", Toast.LENGTH_LONG).show()

                    binding.heartRateEdit.text.length ->
                        Toast.makeText(applicationContext, "입력된 맥박 수치가 없습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.highPressureEdit.addTextChangedListener {
            examineEditTextFull()
        }

        binding.lowPressureEdit.addTextChangedListener {
            examineEditTextFull()
        }

        binding.heartRateEdit.addTextChangedListener {
            examineEditTextFull()
        }

        viewModel.pressureResult.observe(this, Observer {
            val resultIntent = Intent(applicationContext, RecordFragment::class.java)
            resultIntent.putExtra("RELOAD", true)
            setResult(R.integer.record_pressure_result_code, resultIntent)
            finish()
        })

        viewModel.buttonClickEnable.observe(this, Observer {
            if (it) {
                binding.enterBtn.setBackgroundResource(R.drawable.orange_100_2_background)
            } else {
                binding.enterBtn.setBackgroundResource(R.drawable.gray_300_2_background)
            }
        })
    }

    fun initTimeSelector() {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = TimeSelectorFragment()
        transaction.replace(R.id.time_selector_frame, fragment)
        transaction.commit()
    }

    fun examineEditTextFull() {
        viewModel.setButtonClickEnable(binding.highPressureEdit.text.length > 0 && binding.lowPressureEdit.text.length > 0 && binding.heartRateEdit.text.length > 0)
    }
}