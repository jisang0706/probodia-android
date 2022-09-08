package com.example.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.probodia.data.remote.model.PressureDto
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch

class RecordPressureViewModel(val preferenceRepository: PreferenceRepository) : ViewModel() {

    open val serverRepository = ServerRepository()

    private val _pressureResult = MutableLiveData<PressureDto.Record>()
    val pressureResult : LiveData<PressureDto.Record>
        get() = _pressureResult

    fun postPressure(
        timeTag : String, maxPressure : Int,
        minPressure : Int, heartRate : Int,
        recordDate : String) = viewModelScope.launch {
        val accessToken = preferenceRepository.getApiToken().apiAccessToken
        _pressureResult.value = serverRepository.postPressure(accessToken, timeTag, maxPressure, minPressure, heartRate, recordDate)
    }

    fun putPressure(
        recordId : Int,
        timeTag : String, maxPressure : Int,
        minPressure : Int, heartRate : Int,
        recordDate : String) = viewModelScope.launch {
        val accessToken = preferenceRepository.getApiToken().apiAccessToken
        _pressureResult.value = serverRepository.putPressure(accessToken, recordId, timeTag, maxPressure, minPressure, heartRate, recordDate)
    }
}