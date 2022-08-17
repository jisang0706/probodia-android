package com.example.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.probodia.data.remote.model.ApiFoodDto
import com.example.probodia.data.remote.model.MealDto
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch

class RecordMealViewModel(val preferenceRepository : PreferenceRepository) : ViewModel() {

    val serverRepository = ServerRepository()

    private val _mealResult = MutableLiveData<MealDto>()
    val mealResult : LiveData<MealDto>
        get() = _mealResult

    fun postMeal(timeTag : String, foodList : MutableList<ApiFoodDto.Body.FoodItem>, recordDate : String) = viewModelScope.launch {
        val accessToken = preferenceRepository.getApiToken().apiAccessToken
        _mealResult.value = serverRepository.postMeal(accessToken, timeTag, foodList, recordDate)
    }
}