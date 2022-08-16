package com.example.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.probodia.data.remote.model.ApiFoodDto
import com.example.probodia.repository.OpenApiFoodRepository
import kotlinx.coroutines.launch

class SearchFoodViewModel : ViewModel() {

    private val openApiFoodRepository = OpenApiFoodRepository()

    private val _result = MutableLiveData<Pair<Boolean, ApiFoodDto>>()
    val result : LiveData<Pair<Boolean, ApiFoodDto>>
        get() = _result

    fun getFood(isNewData : Boolean, name : String, pageNo : Int) = viewModelScope.launch {
        _result.value = Pair(isNewData, openApiFoodRepository.getFoodList(name, pageNo))
    }
}