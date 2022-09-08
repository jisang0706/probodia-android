package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.data.remote.model.PressureDto
import com.example.probodia.databinding.ActivityRecordPressureBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.view.fragment.RecordDetailFragment
import com.example.probodia.view.fragment.RecordFragment
import com.example.probodia.view.fragment.TimeSelectorFragment
import com.example.probodia.viewmodel.RecordAnythingViewModel
import com.example.probodia.viewmodel.RecordPressureViewModel
import com.example.probodia.viewmodel.factory.RecordAnythingViewModelFactory
import com.example.probodia.viewmodel.factory.RecordPressureViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordPressureActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecordPressureBinding

    private lateinit var pressureViewModel : RecordPressureViewModel
    private lateinit var pressureViewModelFactory : RecordPressureViewModelFactory

    private lateinit var baseViewModel : RecordAnythingViewModel
    private lateinit var baseViewModelFactory: RecordAnythingViewModelFactory

    private var recordType = 0
    private lateinit var data : PressureDto.Record

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(
            applicationContext, R.color.alpha_30
        )

        recordType = intent.getIntExtra("RECORDTYPE", 0)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_pressure)

        pressureViewModelFactory = RecordPressureViewModelFactory(PreferenceRepository(applicationContext))
        pressureViewModel = ViewModelProvider(this, pressureViewModelFactory).get(RecordPressureViewModel::class.java)
        binding.pressureVm = pressureViewModel

        baseViewModelFactory = RecordAnythingViewModelFactory(2)
        baseViewModel = ViewModelProvider(this, baseViewModelFactory).get(RecordAnythingViewModel::class.java)
        binding.baseVm = baseViewModel

        binding.lifecycleOwner = this

        initTimeSelector()

        if (recordType == 1) {
            data = intent.getParcelableExtra("DATA")!!
            baseViewModel.setSelectedTimeTag(when(data.timeTag) {
                "아침" -> 1
                "점심" -> 2
                "저녁" -> 3
                else -> 1
            })
            binding.highPressureEdit.setText(data.maxPressure.toString())
            binding.lowPressureEdit.setText(data.minPressure.toString())
            binding.heartRateEdit.setText(data.heartRate.toString())
            val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            baseViewModel.setLocalDateTime(LocalDateTime.parse(data.recordDate, localDateTimeFormatter))
        }

        binding.enterBtn.setOnClickListener {
            if (baseViewModel.buttonClickEnable.value!!) {
                if (recordType == 1) {
                    pressureViewModel.putPressure(
                        data.recordId,
                        getSelectedTimeTag(),
                        binding.highPressureEdit.text.toString().toInt(),
                        binding.lowPressureEdit.text.toString().toInt(),
                        binding.heartRateEdit.text.toString().toInt(),
                        baseViewModel.localDateTime.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                } else {
                    pressureViewModel.postPressure(
                        getSelectedTimeTag(),
                        binding.highPressureEdit.text.toString().toInt(),
                        binding.lowPressureEdit.text.toString().toInt(),
                        binding.heartRateEdit.text.toString().toInt(),
                        baseViewModel.localDateTime.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                }
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

        pressureViewModel.pressureResult.observe(this, Observer {
            var resultIntent: Intent
            if (recordType == 0) {
                resultIntent = Intent(applicationContext, RecordFragment::class.java)
            } else {
                resultIntent = Intent(applicationContext, RecordDetailFragment::class.java)
            }
            resultIntent.putExtra("RELOAD", true)
            setResult(R.integer.record_pressure_result_code, resultIntent)
            finish()
        })

        baseViewModel.buttonClickEnable.observe(this, Observer {
            if (it) {
                binding.enterBtn.setBackgroundResource(R.drawable.primary_100_2_background)
            } else {
                binding.enterBtn.setBackgroundResource(R.drawable.gray_300_2_background)
            }
        })

        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }

    fun initTimeSelector() {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = TimeSelectorFragment()
        transaction.replace(R.id.time_selector_frame, fragment)
        transaction.commit()
    }

    fun examineEditTextFull() {
        baseViewModel.setButtonClickEnable(binding.highPressureEdit.text.length > 0 && binding.lowPressureEdit.text.length > 0 && binding.heartRateEdit.text.length > 0)
    }

    fun getSelectedTimeTag() = when(baseViewModel.selectedTimeTag.value) {
        1 -> "아침"
        2 -> "점심"
        3 -> "저녁"
        else -> "아침"
    }
}