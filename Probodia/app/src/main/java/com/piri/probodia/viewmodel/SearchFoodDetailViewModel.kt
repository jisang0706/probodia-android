package com.piri.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piri.probodia.R
import com.piri.probodia.data.remote.body.FoodGLBody
import com.piri.probodia.data.remote.model.FoodDetailDto
import com.piri.probodia.data.remote.model.OneGLDto
import com.piri.probodia.repository.AIGlucoseServerRepository
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.repository.ServerFoodRepository
import com.piri.probodia.widget.utils.HttpErrorCode
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

    private val _foodGL = MutableLiveData<OneGLDto>()
    val foodGL : LiveData<OneGLDto>
        get()=  _foodGL

    private val _isGLError = SingleLiveEvent<Boolean>()
    val isGLError : LiveData<Boolean>
        get() = _isGLError

    private val _foodBigInfo = MutableLiveData<FoodDetailDto>()
    val foodBigInfo : LiveData<FoodDetailDto>
        get() = _foodBigInfo

    private val _foodSmallInfo = MutableLiveData<FoodDetailDto>()
    val foodSmallInfo : LiveData<FoodDetailDto>
        get() = _foodSmallInfo

    private val coroutineGLExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _isGLError.call()
    }

    fun getFoodInfo(preferenceRepository : PreferenceRepository, foodId : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val apiToken = preferenceRepository.getApiToken().apiAccessToken
            _foodInfo.value = serverFoodRepository.getFoodDetail(apiToken, foodId)
            _foodQuantityText.value =
                "1인분 (${foodInfo.value!!.quantityByOne}g)당 | ${foodInfo.value!!.calories.roundToInt()} kcal"
        } catch (e : Exception) {
            if (e.localizedMessage != HttpErrorCode.error500) {
                refreshApiToken(preferenceRepository)
                val apiToken = preferenceRepository.getApiToken().apiAccessToken
                _foodInfo.value = serverFoodRepository.getFoodDetail(apiToken, foodId)
                _foodQuantityText.value =
                    "1인분 (${foodInfo.value!!.quantityByOne}g)당 | ${foodInfo.value!!.calories.roundToInt()} kcal"
            }
        }
    }

    fun getFoodGL(preferenceRepository : PreferenceRepository, foodGLBody : FoodGLBody) = viewModelScope.launch(coroutineGLExceptionHandler) {
        try {
            val apiToken = preferenceRepository.getApiToken().apiAccessToken
            _foodGL.value = aiGlucoseServerRepository.getGL(apiToken, foodGLBody)
        } catch (e : Exception) {
            if (e.localizedMessage != HttpErrorCode.error500) {
                val apiToken = preferenceRepository.getApiToken().apiAccessToken
                _foodGL.value = aiGlucoseServerRepository.getGL(apiToken, foodGLBody)
            }
        }
    }

    fun getFoodGLInfo(preferenceRepository : PreferenceRepository, foodBigId : String, foodSmallId : String) = viewModelScope.launch(coroutineExceptionHandler) {
        var apiToken : String
        try {
            apiToken = preferenceRepository.getApiToken().apiAccessToken
            _foodBigInfo.value = serverFoodRepository.getFoodDetail(apiToken, foodBigId)
        } catch (e : Exception) {
            if (e.message == HttpErrorCode.error500) {
                _foodBigInfo.value = FoodDetailDto("", "", "", 0, "", 0.0, 0.0,
                    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
                )
            } else {
                refreshApiToken(preferenceRepository)
                apiToken = preferenceRepository.getApiToken().apiAccessToken
                _foodBigInfo.value = serverFoodRepository.getFoodDetail(apiToken, foodBigId)
            }
        }

        try {
            apiToken = preferenceRepository.getApiToken().apiAccessToken
            _foodSmallInfo.value = serverFoodRepository.getFoodDetail(apiToken, foodSmallId)
        } catch (e : Exception) {
            if (e.message == HttpErrorCode.error500) {
                _foodSmallInfo.value = FoodDetailDto("", "", "", 0, "", 0.0, 0.0,
                    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
                )
            } else {
                refreshApiToken(preferenceRepository)
                apiToken = preferenceRepository.getApiToken().apiAccessToken
                _foodSmallInfo.value = serverFoodRepository.getFoodDetail(apiToken, foodSmallId)
            }
        }
    }

    fun setFoodInfo(foodDetailDto: FoodDetailDto) {
        _foodInfo.value = foodDetailDto
        _foodQuantityText.value = "1인분 (${foodInfo.value!!.quantityByOne}g)당 | ${foodInfo.value!!.calories.roundToInt()} kcal"
    }
}