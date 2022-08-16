package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.probodia.R
import com.example.probodia.data.remote.model.ApiFoodDto
import com.example.probodia.databinding.ActivitySearchFoodDetailBinding

class SearchFoodDetail : AppCompatActivity() {

    private lateinit var binding : ActivitySearchFoodDetailBinding
    private lateinit var item : ApiFoodDto.Body.FoodItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_food_detail)
        binding.lifecycleOwner = this

        item = intent.getParcelableExtra("FOOD")!!

        binding.foodNameText.text = item.name
    }
}