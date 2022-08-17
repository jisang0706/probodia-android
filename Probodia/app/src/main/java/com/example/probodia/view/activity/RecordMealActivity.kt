package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probodia.R
import com.example.probodia.adapter.FoodAddAdapter
import com.example.probodia.data.remote.model.ApiFoodDto
import com.example.probodia.databinding.ActivityRecordMealBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.view.fragment.RecordFragment
import com.example.probodia.view.fragment.TimeSelectorFragment
import com.example.probodia.viewmodel.RecordAnythingViewModel
import com.example.probodia.viewmodel.RecordMealViewModel
import com.example.probodia.viewmodel.factory.RecordAnythingViewModelFactory
import com.example.probodia.viewmodel.factory.RecordMealViewModelFactory
import java.time.format.DateTimeFormatter

class RecordMealActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecordMealBinding

    private lateinit var mealViewModel : RecordMealViewModel
    private lateinit var mealViewModelFactory : RecordMealViewModelFactory

    private lateinit var baseViewModel : RecordAnythingViewModel
    private lateinit var baseViewModelFactory : RecordAnythingViewModelFactory

    private lateinit var listAdapter : FoodAddAdapter

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_meal)

        mealViewModelFactory = RecordMealViewModelFactory(PreferenceRepository(applicationContext))
        mealViewModel = ViewModelProvider(this, mealViewModelFactory).get(RecordMealViewModel::class.java)
        binding.mealVm = mealViewModel

        baseViewModelFactory = RecordAnythingViewModelFactory(4)
        baseViewModel = ViewModelProvider(this, baseViewModelFactory).get(RecordAnythingViewModel::class.java)
        binding.baseVm = baseViewModel

        binding.lifecycleOwner = this

        listAdapter = FoodAddAdapter()
        binding.foodAddRv.adapter = listAdapter
        binding.foodAddRv.layoutManager = LinearLayoutManager(applicationContext)

        listAdapter.setOnItemButtonClickListener(object : FoodAddAdapter.OnItemButtonClickListener {
            override fun onItemDeleteClick(position: Int) {
                baseViewModel.setButtonClickEnable(listAdapter.itemCount > 0)
            }

            override fun onItemEditClick(position: Int) {
                Log.e("FOOD CLICKED", "EDIT")
            }

        })

        binding.searchBtn.setOnClickListener {
            val intent = Intent(applicationContext, SearchFoodActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result : ActivityResult ->
            val intent = result.data
            if (intent != null) {
                if (result.resultCode == R.integer.record_meal_add_code) {
                    val addFood: ApiFoodDto.Body.FoodItem = intent!!.getParcelableExtra("ADDFOOD")!!
                    listAdapter.addItem(addFood)
                    listAdapter.notifyDataSetChanged()
                    baseViewModel.setButtonClickEnable(listAdapter.itemCount > 0)
                }
            }
        }

        initTimeSelector()

        binding.enterBtn.setOnClickListener {
            if (baseViewModel.buttonClickEnable.value!!) {
                mealViewModel.postMeal(
                    when(baseViewModel.selectedTimeTag.value) {
                        1 -> "아침"
                        2 -> "점심"
                        3 -> "저녁"
                        else -> "아침"
                    },
                    listAdapter.getList(),
                    baseViewModel.localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
            } else {
                Toast.makeText(applicationContext, "입력된 식단이 없습니다.", Toast.LENGTH_LONG).show()
            }
        }

        mealViewModel.mealResult.observe(this, Observer {
            val resultIntent = Intent(applicationContext, RecordFragment::class.java)
            resultIntent.putExtra("RELOAD", true)
            setResult(R.integer.record_meal_result_code, resultIntent)
            finish()
        })

        baseViewModel.buttonClickEnable.observe(this, Observer {
            if (it) {
                binding.enterBtn.setBackgroundResource(R.drawable.primary_100_2_background)
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