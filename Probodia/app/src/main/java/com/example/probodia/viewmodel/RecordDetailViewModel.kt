package com.example.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch

class RecordDetailViewModel(val recordType : String) : ViewModel() {

    val serverRepository = ServerRepository()

    private val _deleteResult = MutableLiveData<Int>()
    val deleteResult : LiveData<Int>
        get() = _deleteResult

    private val _titleText = MutableLiveData<String>(when(recordType) {
        "SUGAR" -> "혈당"
        "PRESSURE" -> "혈압"
        "MEDICINE" -> "투약"
        "MEAL" -> "음식"
        else -> ""
    })
    val titleText : LiveData<String>
        get() = _titleText

    fun deleteRecord(preferenceRepository : PreferenceRepository, recordId : Int) = viewModelScope.launch {
        val accessToken = preferenceRepository.getApiToken().apiAccessToken
        _deleteResult.value = when(recordType) {
            "SUGAR" -> serverRepository.deleteGlucose(accessToken, recordId)
            "PRESSURE" -> serverRepository.deletePressure(accessToken, recordId)
            "MEDICINE" -> serverRepository.deleteMedicine(accessToken, recordId)
            "MEAL" -> serverRepository.deleteMeal(accessToken, recordId)
            else -> 0
        }
    }
}