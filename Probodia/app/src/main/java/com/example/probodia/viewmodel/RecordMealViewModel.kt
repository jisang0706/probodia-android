package com.example.probodia.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.probodia.data.remote.body.PostMealBody
import com.example.probodia.data.remote.model.FoodNamesDto
import com.example.probodia.data.remote.model.MealDto
import com.example.probodia.repository.AIServerRepository
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class RecordMealViewModel(val preferenceRepository : PreferenceRepository) : ViewModel() {

    val serverRepository = ServerRepository()
    val aiServerRepository = AIServerRepository()

    private val _mealResult = MutableLiveData<MealDto>()
    val mealResult : LiveData<MealDto>
        get() = _mealResult

    private val _foodNamesResult = MutableLiveData<FoodNamesDto>()
    val foodNamesResult : LiveData<FoodNamesDto>
        get() = _foodNamesResult

    private val _foodImage = MutableLiveData<Bitmap>()
    val foodImage : LiveData<Bitmap>
        get() = _foodImage

    fun postMeal(timeTag : String, foodList : MutableList<PostMealBody.PostMealItem>, recordDate : String) = viewModelScope.launch {
        val accessToken = preferenceRepository.getApiToken().apiAccessToken
        _mealResult.value = serverRepository.postMeal(accessToken, timeTag, foodList, recordDate)
    }

    fun getImageFood(filename : String) = viewModelScope.launch {
        _foodNamesResult.value = aiServerRepository.getImageFood(filename)
    }

    fun setFoodImage(bitmap : Bitmap) {
        _foodImage.value = bitmap
    }
}