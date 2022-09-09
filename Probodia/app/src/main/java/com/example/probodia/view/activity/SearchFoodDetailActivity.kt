package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probodia.R
import com.example.probodia.adapter.FoodInfoAdapter
import com.example.probodia.data.remote.body.PostMealBody
import com.example.probodia.data.remote.model.FoodDetailDto
import com.example.probodia.databinding.ActivitySearchFoodDetailBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.viewmodel.SearchFoodDetailViewModel
import kotlin.math.roundToInt

class SearchFoodDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchFoodDetailBinding
    private lateinit var listAdapter : FoodInfoAdapter

    private lateinit var viewModel : SearchFoodDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(
            applicationContext, R.color.alpha_30
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_food_detail)
        binding.lifecycleOwner = this

        val foodId = intent.getStringExtra("FOODID")!!

        viewModel = SearchFoodDetailViewModel()
        binding.vm = viewModel

        viewModel.getFoodInfo(PreferenceRepository(applicationContext), foodId)

        viewModel.foodInfo.observe(this, Observer {
            binding.foodNameText.text = it.name

            listAdapter = FoodInfoAdapter(getFoodInfoList(it))
            binding.foodInfoRv.adapter = listAdapter
            binding.foodInfoRv.layoutManager = LinearLayoutManager(applicationContext)
        })

        binding.enterBtn.setOnClickListener {
            val resultIntent = Intent(applicationContext, SearchFoodActivity::class.java)
            if ("${binding.quantityEdit.text}" == "")    binding.quantityEdit.setText("1")
            val postMealItem = PostMealBody.PostMealItem(
                viewModel.foodInfo.value!!.name,
                foodId,
                viewModel.foodInfo.value!!.quantityByOne * "${binding.quantityEdit.text}".toInt(),
                viewModel.foodInfo.value!!.calories.roundToInt(),
                0,
                ""
            )
            resultIntent.putExtra("ADDFOOD", postMealItem)
            setResult(R.integer.record_meal_add_code, resultIntent)
            finish()
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }

    fun getFoodInfoList(item : FoodDetailDto) : List<Pair<String, Double>> {
        return listOf(
            Pair("탄수화물", item.carbohydrate),
            Pair("당류", item.sugars),
            Pair("단백질", item.protein),
            Pair("지방", item.fat),
            Pair("콜레스테롤", item.cholesterol),
            Pair("나트륨", item.salt)
        )
    }
}