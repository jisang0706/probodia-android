package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probodia.R
import com.example.probodia.adapter.FoodSearchAdapter
import com.example.probodia.databinding.ActivitySearchFoodBinding
import com.example.probodia.viewmodel.SearchFoodViewModel

class SearchFoodActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchFoodBinding

    private lateinit var viewModel : SearchFoodViewModel

    private lateinit var listAdapter : FoodSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_food)
        viewModel = ViewModelProvider(this).get(SearchFoodViewModel::class.java)
        binding.lifecycleOwner = this

        listAdapter = FoodSearchAdapter()
        binding.foodRv.adapter = listAdapter
        binding.foodRv.layoutManager = LinearLayoutManager(applicationContext)

        binding.tempSearchBtn.setOnClickListener {
            viewModel.getFood(true, binding.foodEdittext.text.toString(), 1)
        }

        viewModel.result.observe(this, Observer {
            if (it.first) {
                listAdapter.resetDataSet()
            }

            listAdapter.addDataSet(it.second.body.items)
            listAdapter.notifyDataSetChanged()
        })

        listAdapter.setOnItemClickListener(object : FoodSearchAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                val intent = Intent(applicationContext, SearchFoodDetailAdapter::class.java)
                intent.putExtra("FOOD", listAdapter.dataSet[position])
                startActivity(intent)
            }

        })
    }
}