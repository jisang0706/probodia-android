package com.piri.probodia.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.body.FoodAllGLBody
import com.piri.probodia.data.remote.body.PostMealBody
import com.piri.probodia.data.remote.model.AllGLDto
import com.piri.probodia.data.remote.model.FoodNamesDto
import com.piri.probodia.data.remote.model.MealDto
import com.piri.probodia.repository.AIGlucoseServerRepository
import com.piri.probodia.repository.AIServerRepository
import com.piri.probodia.repository.PreferenceRepository
import kotlinx.coroutines.launch

class RecordMealViewModel() : BaseViewModel() {

    private val aiServerRepository = AIServerRepository()
    private val aiGlucoseServerRepository = AIGlucoseServerRepository()

    private val _mealResult = MutableLiveData<MealDto.Record>()
    val mealResult : LiveData<MealDto.Record>
        get() = _mealResult

    private val _foodNamesResult = MutableLiveData<FoodNamesDto>()
    val foodNamesResult : LiveData<FoodNamesDto>
        get() = _foodNamesResult

    private val _foodImage = MutableLiveData<Bitmap>()
    val foodImage : LiveData<Bitmap>
        get() = _foodImage

    private val _foodGL = MutableLiveData<AllGLDto>()
    val foodGL : LiveData<AllGLDto>
        get() = _foodGL


    fun postMeal(preferenceRepo : PreferenceRepository, timeTag : String, foodList : MutableList<PostMealBody.PostMealItem>, recordDate : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _postMeal(accessToken, timeTag, foodList, recordDate)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _postMeal(accessToken, timeTag, foodList, recordDate)
        }
    }

    suspend fun _postMeal(accessToken : String, timeTag : String, foodList : MutableList<PostMealBody.PostMealItem>, recordDate : String) {
        _mealResult.value =
            serverRepository.postMeal(accessToken, timeTag, foodList, recordDate)
    }

    fun getImageFood(preferenceRepo : PreferenceRepository, filename : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _foodNamesResult.value = aiServerRepository.getImageFood(accessToken, filename)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _foodNamesResult.value = aiServerRepository.getImageFood(accessToken, filename)
        }
    }

    fun setFoodImage(bitmap : Bitmap) {
        _foodImage.value = bitmap
    }

    fun putMeal(preferenceRepo : PreferenceRepository, recordId : Int, timeTag : String, foodList : MutableList<PostMealBody.PostMealItem>, recordDate : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _putMeal(accessToken, recordId, timeTag, recordDate, foodList)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _putMeal(accessToken, recordId, timeTag, recordDate, foodList)
        }
    }

    suspend fun _putMeal(accessToken : String, recordId : Int, timeTag : String, recordDate : String, foodList : MutableList<PostMealBody.PostMealItem>) {
        _mealResult.value = serverRepository.putMeal(accessToken, recordId, timeTag, recordDate, foodList)
    }

    fun getFoodGL(preferenceRepo: PreferenceRepository, foodAllGLBody: FoodAllGLBody) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val apiToken = preferenceRepo.getApiToken().apiAccessToken
            _foodGL.value = aiGlucoseServerRepository.getAllGL(apiToken, foodAllGLBody)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val apiToken = preferenceRepo.getApiToken().apiAccessToken
            _foodGL.value = aiGlucoseServerRepository.getAllGL(apiToken, foodAllGLBody)
        }
    }
}