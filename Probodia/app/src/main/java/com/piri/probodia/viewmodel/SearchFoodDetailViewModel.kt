package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.model.FoodDetailDto
import com.piri.probodia.data.remote.model.GLDto
import com.piri.probodia.repository.AIGlucoseServerRepository
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.repository.ServerFoodRepository
import com.piri.probodia.widget.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class SearchFoodDetailViewModel : BaseViewModel() {

    private val serverFoodRepository = ServerFoodRepository()
    private val aiGlucoseServerRepository = AIGlucoseServerRepository()

    private val _foodInfo = MutableLiveData<FoodDetailDto>()
    val foodInfo : LiveData<FoodDetailDto>
        get() = _foodInfo

    private val _foodQuantityText = MutableLiveData("")
    val foodQuantityText : LiveData<String>
        get() = _foodQuantityText

    private val _foodGL = MutableLiveData<GLDto>()
    val foodGL : LiveData<GLDto>
        get()=  _foodGL

    private val _isGLError = SingleLiveEvent<Boolean>()
    val isGLError : LiveData<Boolean>
        get() = _isGLError

    val coroutineGLExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _isGLError.call()
    }

    fun getFoodInfo(preferenceRepository : PreferenceRepository, foodId : String) = viewModelScope.launch(coroutineExceptionHandler) {
        val apiToken = preferenceRepository.getApiToken().apiAccessToken
        _foodInfo.value = serverFoodRepository.getFoodDetail(apiToken, foodId)
        _foodQuantityText.value = "1인분 (${foodInfo.value!!.quantityByOne}g)당 | ${foodInfo.value!!.calories.roundToInt()} kcal"
    }

    fun getFoodGL(preferenceRepository : PreferenceRepository, foodDetailBody : FoodDetailDto) = viewModelScope.launch(coroutineGLExceptionHandler) {
        val apiToken = preferenceRepository.getApiToken().apiAccessToken
        _foodGL.value = aiGlucoseServerRepository.getGL(apiToken, foodDetailBody)
    }
}