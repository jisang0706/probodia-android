package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.databinding.ActivityRecordGlucoseBinding
import com.example.probodia.view.fragment.RecordFragment
import com.example.probodia.viewmodel.RecordGlucoseViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordGlucoseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecordGlucoseBinding
    private lateinit var viewModel: RecordGlucoseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_glucose)
        viewModel = ViewModelProvider(this).get(RecordGlucoseViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.enterBtn.setOnClickListener {
            viewModel.postGlucose(
                "아침 식전",
                binding.glucoseEdit.text.toString().toInt(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        }

        viewModel.result.observe(this, Observer {
            val resultIntent = Intent(applicationContext, RecordFragment::class.java)
            resultIntent.putExtra("RELOAD", true)
            setResult(R.integer.record_glucose_result_code, resultIntent)
            finish()
        })
    }
}