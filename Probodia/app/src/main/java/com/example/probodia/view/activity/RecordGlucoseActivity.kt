package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.databinding.ActivityRecordGlucoseBinding
import com.example.probodia.view.fragment.RecordFragment
import com.example.probodia.viewmodel.RecordGlucoseViewModel
import java.time.LocalDateTime

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
            val resultIntent = Intent(applicationContext, RecordFragment::class.java)
            resultIntent.putExtra("KIND", "아침 식전")
            resultIntent.putExtra("GLUCOSE", 100)
            setResult(R.integer.record_glucose_result_code, resultIntent)
            finish()
        }
    }
}