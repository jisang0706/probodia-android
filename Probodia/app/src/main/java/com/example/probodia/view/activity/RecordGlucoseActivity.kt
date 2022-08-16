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
import com.example.probodia.databinding.ActivityRecordGlucoseBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.view.fragment.RecordFragment
import com.example.probodia.view.fragment.TimeSelectorFragment
import com.example.probodia.viewmodel.RecordAnythingViewModel
import com.example.probodia.viewmodel.RecordGlucoseViewModel
import com.example.probodia.viewmodel.factory.RecordAnythingViewModelFactory
import com.example.probodia.viewmodel.factory.RecordGlucoseViewModelFactory
import java.time.format.DateTimeFormatter

class RecordGlucoseActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecordGlucoseBinding

    private lateinit var glucoseViewModel : RecordGlucoseViewModel
    private lateinit var glucoseViewModelFactory : RecordGlucoseViewModelFactory

    private lateinit var baseViewModel : RecordAnythingViewModel
    private lateinit var baseViewModelFactory : RecordAnythingViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_glucose)

        glucoseViewModelFactory = RecordGlucoseViewModelFactory(PreferenceRepository(applicationContext))
        glucoseViewModel = ViewModelProvider(this, glucoseViewModelFactory).get(RecordGlucoseViewModel::class.java)
        binding.glucoseVm = glucoseViewModel

        baseViewModelFactory = RecordAnythingViewModelFactory(1)
        baseViewModel = ViewModelProvider(this, baseViewModelFactory).get(RecordAnythingViewModel::class.java)
        binding.baseVm = baseViewModel

        binding.lifecycleOwner = this

        initTimeSelector()

        binding.enterBtn.setOnClickListener {
            if (baseViewModel.buttonClickEnable.value!!) {
                glucoseViewModel.postGlucose(
                    when(baseViewModel.selectedTimeTag.value) {
                        1 -> "아침 식전"
                        2 -> "점심 식전"
                        3 -> "저녁 식전"
                        4 -> "아침 식후"
                        5 -> "점심 식후"
                        6 -> "저녁 식후"
                        else -> "아침 식전"
                    },
                    binding.glucoseEdit.text.toString().toInt(),
                    baseViewModel.localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
            } else {
                Toast.makeText(applicationContext, "입력된 혈당 수치가 없습니다.", Toast.LENGTH_LONG).show()
            }
        }

        binding.glucoseEdit.addTextChangedListener {
            baseViewModel.setButtonClickEnable(it?.length!! > 0)
        }

        glucoseViewModel.glucoseResult.observe(this, Observer {
            val resultIntent = Intent(applicationContext, RecordFragment::class.java)
            resultIntent.putExtra("RELOAD", true)
            setResult(R.integer.record_glucose_result_code, resultIntent)
            finish()
        })

        baseViewModel.buttonClickEnable.observe(this, Observer {
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
}