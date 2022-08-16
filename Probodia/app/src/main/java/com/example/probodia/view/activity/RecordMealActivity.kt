package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.databinding.ActivityRecordMealBinding
import com.example.probodia.view.fragment.TimeSelectorFragment
import com.example.probodia.viewmodel.RecordAnythingViewModel
import com.example.probodia.viewmodel.RecordMealViewModel
import com.example.probodia.viewmodel.factory.RecordAnythingViewModelFactory

class RecordMealActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecordMealBinding

    private lateinit var mealViewModel : RecordMealViewModel

    private lateinit var baseViewModel : RecordAnythingViewModel
    private lateinit var baseViewModelFactory : RecordAnythingViewModelFactory

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_meal)

        mealViewModel = ViewModelProvider(this).get(RecordMealViewModel::class.java)
        binding.mealVm = mealViewModel

        baseViewModelFactory = RecordAnythingViewModelFactory(4)
        baseViewModel = ViewModelProvider(this, baseViewModelFactory).get(RecordAnythingViewModel::class.java)
        binding.baseVm = baseViewModel

        binding.lifecycleOwner = this

        binding.searchBtn.setOnClickListener {
            val intent = Intent(applicationContext, SearchFoodActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result : ActivityResult ->
            val intent = result.data
            if (intent != null) {
                val addFood = intent!!.getBooleanExtra("ADDFOOD", false)
                if (addFood) {

                }
            }
        }

        initTimeSelector()
    }

    fun initTimeSelector() {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = TimeSelectorFragment()
        transaction.replace(R.id.time_selector_frame, fragment)
        transaction.commit()
    }
}