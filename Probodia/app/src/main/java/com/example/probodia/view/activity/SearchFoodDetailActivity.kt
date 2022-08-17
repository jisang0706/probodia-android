package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probodia.R
import com.example.probodia.adapter.FoodInfoAdapter
import com.example.probodia.data.remote.model.ApiFoodDto
import com.example.probodia.databinding.ActivitySearchFoodDetailBinding

class SearchFoodDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchFoodDetailBinding
    private lateinit var listAdapter : FoodInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_food_detail)
        binding.lifecycleOwner = this

        val item : ApiFoodDto.Body.FoodItem = intent.getParcelableExtra("FOOD")!!

        binding.foodNameText.text = item.name

        listAdapter = FoodInfoAdapter(getFoodInfoList(item))
        binding.foodInfoRv.adapter = listAdapter
        binding.foodInfoRv.layoutManager = LinearLayoutManager(applicationContext)

        binding.enterBtn.setOnClickListener {
            val resultIntent = Intent(applicationContext, SearchFoodActivity::class.java)
            resultIntent.putExtra("ADDFOOD", item)
            setResult(R.integer.record_meal_add_code, resultIntent)
            finish()
        }
    }

    fun getFoodInfoList(item : ApiFoodDto.Body.FoodItem) : List<Pair<String, Double>> {
        return listOf(
            Pair("탄수화물", item.carbohydrate.toDouble()),
            Pair("당류", item.sugar.toDouble()),
            Pair("단백질", item.protein.toDouble()),
            Pair("지방", item.fat.toDouble()),
            Pair("콜레스테롤", item.cholesterol.toDouble()),
            Pair("나트륨", item.sodium.toDouble())
        )
    }
}