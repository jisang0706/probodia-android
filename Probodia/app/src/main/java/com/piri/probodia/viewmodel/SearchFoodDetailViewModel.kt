package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.model.FoodDetailDto
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.repository.ServerFoodRepository
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class SearchFoodDetailViewModel : ViewModel() {

    private val serverFoodRepository = ServerFoodRepository()

    private val _foodInfo = MutableLiveData<FoodDetailDto>()
    val foodInfo : LiveData<FoodDetailDto>
        get() = _foodInfo

    private val _foodQuantityText = MutableLiveData("")
    val foodQuantityText : LiveData<String>
        get() = _foodQuantityText

    fun getFoodInfo(preferenceRepository : PreferenceRepository, foodId : String) = viewModelScope.launch {
        val apiToken = preferenceRepository.getApiToken().apiAccessToken
        _foodInfo.value = serverFoodRepository.getFoodDetail(apiToken, foodId)
        _foodQuantityText.value = "1인분 (${foodInfo.value!!.quantityByOne}g)당 | ${foodInfo.value!!.calories.roundToInt()} kcal"
    }
}