package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.databinding.ActivityRecordMedicineBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.view.fragment.TimeSelectorFragment
import com.example.probodia.viewmodel.RecordAnythingViewModel
import com.example.probodia.viewmodel.RecordMedicineViewModel
import com.example.probodia.viewmodel.factory.RecordAnythingViewModelFactory

class RecordMedicineActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecordMedicineBinding

    private lateinit var medicineViewModel : RecordMedicineViewModel

    private lateinit var baseViewModel : RecordAnythingViewModel
    private lateinit var baseViewModelFactory : RecordAnythingViewModelFactory

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(
            applicationContext, R.color.alpha_30
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_medicine)

        medicineViewModel = ViewModelProvider(this).get(RecordMedicineViewModel::class.java)
        binding.medicineVm = medicineViewModel

        baseViewModelFactory = RecordAnythingViewModelFactory(3)
        baseViewModel = ViewModelProvider(this, baseViewModelFactory).get(RecordAnythingViewModel::class.java)
        binding.baseVm = baseViewModel

        binding.lifecycleOwner = this

        initTimeSelector()

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
}