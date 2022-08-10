package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.databinding.ActivityRecordPressureBinding
import com.example.probodia.view.fragment.RecordFragment
import com.example.probodia.viewmodel.RecordPressureViewModel

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
            val resultIntent = Intent(applicationContext, RecordFragment::class.java)
            resultIntent.putExtra("HIGH", 120)
            resultIntent.putExtra("LOW", 80)
            setResult(R.integer.record_pressure_result_code, resultIntent)
            finish()
        }
    }
}