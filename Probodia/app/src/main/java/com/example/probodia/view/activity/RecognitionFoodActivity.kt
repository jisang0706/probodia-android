package com.example.probodia.view.activity

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.databinding.ActivityRecognitionFoodBinding
import com.example.probodia.viewmodel.RecognitionFoodViewModel
import com.example.probodia.viewmodel.factory.RecognitionFoodViewModelFactory

class RecognitionFoodActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecognitionFoodBinding

    private lateinit var viewModel : RecognitionFoodViewModel
    private lateinit var viewModelFactory : RecognitionFoodViewModelFactory

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
    }
}