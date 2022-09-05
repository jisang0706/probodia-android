package com.example.probodia.view.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.data.remote.body.PostMealBody
import com.example.probodia.databinding.ActivityRecognitionFoodBinding
import com.example.probodia.viewmodel.RecognitionFoodViewModel
import com.example.probodia.viewmodel.factory.RecognitionFoodViewModelFactory

class RecognitionFoodActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecognitionFoodBinding

    private lateinit var viewModel : RecognitionFoodViewModel
    private lateinit var viewModelFactory : RecognitionFoodViewModelFactory

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        window.statusBarColor = ContextCompat.getColor(
            applicationContext, R.color.alpha_30
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recognition_food)

        val foodImage = intent.getParcelableExtra<Bitmap>("foodImage")!!
        binding.foodImage.setImageBitmap(foodImage)

        viewModelFactory = RecognitionFoodViewModelFactory(
            intent.getStringArrayExtra("foodNames")!!.toList())
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecognitionFoodViewModel::class.java)
        binding.vm = viewModel

        viewModel.selectedFood.observe(this, Observer {
            binding.food1Btn.setBackgroundResource(R.drawable.white_1_background)
            binding.food1Btn.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray_700))
            binding.food2Btn.setBackgroundResource(R.drawable.white_1_background)
            binding.food2Btn.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray_700))
            binding.food3Btn.setBackgroundResource(R.drawable.white_1_background)
            binding.food3Btn.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray_700))

            when(it) {
                0 -> {
                    binding.food1Btn.setBackgroundResource(R.drawable.primary_100_1_background)
                    binding.food1Btn.setTextColor(Color.WHITE)
                }
                1 -> {
                    binding.food2Btn.setBackgroundResource(R.drawable.primary_100_1_background)
                    binding.food2Btn.setTextColor(Color.WHITE)
                }
                2 -> {
                    binding.food3Btn.setBackgroundResource(R.drawable.primary_100_1_background)
                    binding.food3Btn.setTextColor(Color.WHITE)
                }
            }
        })

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result : ActivityResult ->
            val intent = result.data
            if (intent != null) {
                if (result.resultCode == R.integer.record_meal_add_code) {
                    val item : PostMealBody.PostMealItem = intent!!.getParcelableExtra("ADDFOOD")!!
                    applyItem(item)
                }
            }
        }

        binding.enterBtn.setOnClickListener {
            val intent = Intent(applicationContext, SearchFoodActivity::class.java)
            intent.putExtra("foodName", viewModel.foodNameResult.value!![viewModel.selectedFood.value!!])
            intent.putExtra("imageSearch", true)
            activityResultLauncher.launch(intent)
        }

        binding.searchBtn.setOnClickListener {
            val intent = Intent(applicationContext, SearchFoodActivity::class.java)
            intent.putExtra("imageSearch", true)
            activityResultLauncher.launch(intent)
        }
    }

    fun applyItem(item : PostMealBody.PostMealItem) {
        val resultIntent = Intent(applicationContext, RecordMealActivity::class.java)
        resultIntent.putExtra("ADDFOOD", item)
        setResult(R.integer.record_meal_add_code, resultIntent)
        finish()
    }
}