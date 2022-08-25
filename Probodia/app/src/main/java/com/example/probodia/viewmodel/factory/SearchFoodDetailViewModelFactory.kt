package com.example.probodia.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.data.remote.model.ApiFoodDto
import com.example.probodia.viewmodel.SearchFoodDetailViewModel

class SearchFoodDetailViewModelFactory(private val foodItem : ApiFoodDto.Body.FoodItem) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchFoodDetailViewModel::class.java)) {
            return SearchFoodDetailViewModel(foodItem) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}