package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.databinding.ActivityRecordPressureBinding
import com.example.probodia.view.fragment.RecordFragment
import com.example.probodia.viewmodel.RecordPressureViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordPressureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecordPressureBinding
    private lateinit var viewModel: RecordPressureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_pressure)
        viewModel = ViewModelProvider(this).get(RecordPressureViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.enterBtn.setOnClickListener {
            viewModel.postPressure(
                "아침",
                binding.highPressureEdit.text.toString().toInt(),
                binding.lowPressureEdit.text.toString().toInt(),
                binding.heartRateEdit.text.toString().toInt(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        }

        viewModel.result.observe(this, Observer {
            val resultIntent = Intent(applicationContext, RecordFragment::class.java)
            resultIntent.putExtra("RELOAD", true)
            setResult(R.integer.record_pressure_result_code, resultIntent)
            finish()
        })
    }
}