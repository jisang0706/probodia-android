package com.piri.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecognitionFoodViewModel(foodNames : List<String>) : ViewModel() {

    private val _foodNameResult = MutableLiveData(foodNames)
    val foodNameResult : LiveData<List<String>>
        get() = _foodNameResult

    private val _selectedFood = MutableLiveData(0)
    val selectedFood : LiveData<Int>
        get() = _selectedFood

    fun setSelectedFood(num : Int) {
        _selectedFood.value = num
    }
}