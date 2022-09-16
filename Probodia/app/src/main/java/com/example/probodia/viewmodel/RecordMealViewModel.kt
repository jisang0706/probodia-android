package com.example.probodia.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.probodia.data.remote.body.PostMealBody
import com.example.probodia.data.remote.body.PutMealBody
import com.example.probodia.data.remote.model.FoodDetailDto
import com.example.probodia.data.remote.model.FoodNamesDto
import com.example.probodia.data.remote.model.MealDto
import com.example.probodia.repository.AIServerRepository
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerFoodRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class RecordMealViewModel(val preferenceRepository : PreferenceRepository) : TokenViewModel() {

    val aiServerRepository = AIServerRepository()

    private val _mealResult = MutableLiveData<MealDto.Record>()
    val mealResult : LiveData<MealDto.Record>
        get() = _mealResult

    private val _foodNamesResult = MutableLiveData<FoodNamesDto>()
    val foodNamesResult : LiveData<FoodNamesDto>
        get() = _foodNamesResult

    private val _foodImage = MutableLiveData<Bitmap>()
    val foodImage : LiveData<Bitmap>
        get() = _foodImage


    fun postMeal(timeTag : String, foodList : MutableList<PostMealBody.PostMealItem>, recordDate : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _postMeal(accessToken, timeTag, foodList, recordDate)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepository)
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _postMeal(accessToken, timeTag, foodList, recordDate)
        }
    }

    suspend fun _postMeal(accessToken : String, timeTag : String, foodList : MutableList<PostMealBody.PostMealItem>, recordDate : String) {
        _mealResult.value =
            serverRepository.postMeal(accessToken, timeTag, foodList, recordDate)
    }

    fun getImageFood(filename : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _foodNamesResult.value = aiServerRepository.getImageFood(accessToken, filename)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepository)
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _foodNamesResult.value = aiServerRepository.getImageFood(accessToken, filename)
        }
    }

    fun setFoodImage(bitmap : Bitmap) {
        _foodImage.value = bitmap
    }

    fun putMeal(recordId : Int, timeTag : String, foodList : MutableList<PostMealBody.PostMealItem>, recordDate : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _putMeal(accessToken, recordId, timeTag, recordDate, foodList)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepository)
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _putMeal(accessToken, recordId, timeTag, recordDate, foodList)
        }
    }

    suspend fun _putMeal(accessToken : String, recordId : Int, timeTag : String, recordDate : String, foodList : MutableList<PostMealBody.PostMealItem>) {
        _mealResult.value = serverRepository.putMeal(accessToken, recordId, timeTag, recordDate, foodList)
    }
}