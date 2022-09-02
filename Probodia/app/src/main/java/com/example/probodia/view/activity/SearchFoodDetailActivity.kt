package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probodia.R
import com.example.probodia.adapter.FoodInfoAdapter
import com.example.probodia.data.remote.model.ApiFoodDto
import com.example.probodia.databinding.ActivitySearchFoodDetailBinding
import com.example.probodia.viewmodel.SearchFoodDetailViewModel
import com.example.probodia.viewmodel.factory.SearchFoodDetailViewModelFactory

class SearchFoodDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchFoodDetailBinding
    private lateinit var listAdapter : FoodInfoAdapter

    private lateinit var viewModel : SearchFoodDetailViewModel
    private lateinit var viewModelFactory : SearchFoodDetailViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(
            applicationContext, R.color.alpha_30
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_food_detail)
        binding.lifecycleOwner = this

        val item : ApiFoodDto.Body.FoodItem = intent.getParcelableExtra("FOOD")!!

        viewModelFactory = SearchFoodDetailViewModelFactory(item)
        viewModel = SearchFoodDetailViewModel(item)
        binding.vm = viewModel

        binding.foodNameText.text = item.itemName

        listAdapter = FoodInfoAdapter(getFoodInfoList(item))
        binding.foodInfoRv.adapter = listAdapter
        binding.foodInfoRv.layoutManager = LinearLayoutManager(applicationContext)

        binding.enterBtn.setOnClickListener {
            val resultIntent = Intent(applicationContext, SearchFoodActivity::class.java)
            resultIntent.putExtra("ADDFOOD", item)
            setResult(R.integer.record_meal_add_code, resultIntent)
            finish()
        }

        binding.cancelBtn.setOnClickListener {
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