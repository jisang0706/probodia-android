package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.db.FoodDatabase
import com.piri.probodia.data.db.entity.FoodEntity
import com.piri.probodia.data.remote.model.FoodDto
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.repository.ServerFoodRepository
import com.piri.probodia.widget.utils.HttpErrorCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class SearchFoodViewModel : BaseViewModel() {

    private val serverFoodRepository = ServerFoodRepository()

    private val _foodName = MutableLiveData<String>()
    val foodname : LiveData<String>
        get() = _foodName

    private val _result = MutableLiveData<Pair<Boolean, FoodDto>>()
    val result : LiveData<Pair<Boolean, FoodDto>>
        get() = _result

    private val _foodDbResult = MutableLiveData<List<FoodEntity>>()
    val foodDbResult : LiveData<List<FoodEntity>>
        get() = _foodDbResult

    fun getFood(isNewData : Boolean, preferenceRepository : PreferenceRepository, name : String, pageNo : Int) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val apiToken = preferenceRepository.getApiToken().apiAccessToken
            _result.value =
                Pair(isNewData, serverFoodRepository.getFoodList(apiToken, name, pageNo))
        } catch (e : Exception) {
            if (e.localizedMessage != HttpErrorCode.error500) {
                refreshApiToken(preferenceRepository)
                val apiToken = preferenceRepository.getApiToken().apiAccessToken
                _result.value =
                    Pair(isNewData, serverFoodRepository.getFoodList(apiToken, name, pageNo))
            }
        }
    }

    fun setFoodName(name : String) {
        _foodName.value = name
    }

    fun saveFoodDb(database : FoodDatabase, food : FoodDto.FoodItem) = CoroutineScope(Dispatchers.IO).launch {
        database.foodDao().deleteByFoodId(food.foodId)
        database.foodDao().insert(FoodEntity(0, food.itemName, food.foodId))
        database.foodDao().deletePast20()
    }

    fun getFoodDb(database : FoodDatabase) = CoroutineScope(Dispatchers.IO).launch {
        _foodDbResult.postValue(database.foodDao().getAllFood())
    }
}