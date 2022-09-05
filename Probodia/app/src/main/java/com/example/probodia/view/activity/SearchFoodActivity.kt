package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probodia.R
import com.example.probodia.adapter.SearchAdapter
import com.example.probodia.data.remote.body.PostMealBody
import com.example.probodia.data.remote.model.ApiItemName
import com.example.probodia.data.remote.model.FoodDto
import com.example.probodia.databinding.ActivitySearchFoodBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.viewmodel.SearchFoodViewModel

class SearchFoodActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchFoodBinding
    private lateinit var viewModel : SearchFoodViewModel
    private lateinit var listAdapter : SearchAdapter
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(
            applicationContext, R.color.alpha_30
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_food)
        viewModel = ViewModelProvider(this).get(SearchFoodViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        listAdapter = SearchAdapter()
        binding.foodRv.adapter = listAdapter
        binding.foodRv.layoutManager = LinearLayoutManager(applicationContext)

        val foodName = intent.getStringExtra("foodName")
        if (foodName != "") {
            binding.foodEdittext.setText(foodName, TextView.BufferType.EDITABLE)
        }

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

        binding.foodEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.getFood(true, PreferenceRepository(applicationContext), binding.foodEdittext.text.toString(), 1)
            }

        })

        viewModel.result.observe(this, Observer {
            if (it.first) {
                listAdapter.resetDataSet()
            }

            listAdapter.addDataSet(it.second.data as MutableList<ApiItemName>)
            listAdapter.notifyDataSetChanged()
        })

        listAdapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                val intent = Intent(applicationContext, SearchFoodDetailActivity::class.java)
                intent.putExtra("FOODID", (listAdapter.dataSet[position] as FoodDto.FoodItem).foodId)
                activityResultLauncher.launch(intent)
            }

        })

        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }

    fun applyItem(item : PostMealBody.PostMealItem) {
        var resultIntent : Intent
        if (intent.getBooleanExtra("imageSearch", false)) {
            resultIntent = Intent(applicationContext, RecognitionFoodActivity::class.java)
        } else {
            resultIntent = Intent(applicationContext, RecordMealActivity::class.java)
        }
        resultIntent.putExtra("ADDFOOD", item)
        setResult(R.integer.record_meal_add_code, resultIntent)
        finish()
    }
}