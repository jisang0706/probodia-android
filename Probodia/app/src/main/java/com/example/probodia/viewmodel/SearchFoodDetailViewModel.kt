package com.example.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.probodia.data.remote.model.ApiFoodDto

class SearchFoodDetailViewModel(food : ApiFoodDto.Body.FoodItem) : ViewModel() {

    private val _foodInfo = MutableLiveData(food)
    val foodInfo : LiveData<ApiFoodDto.Body.FoodItem>
        get() = _foodInfo

    private val _foodQuantityText = MutableLiveData(
        "1인분 (${foodInfo.value!!.quantity})당 | ${foodInfo.value!!.kcal} kcal")
    val foodQuantityText : LiveData<String>
        get() = _foodQuantityText
}