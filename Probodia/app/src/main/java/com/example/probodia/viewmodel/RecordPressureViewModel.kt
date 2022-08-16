package com.example.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.probodia.data.remote.model.PressureDto
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch

public class RecordPressureViewModel(val preferenceRepository: PreferenceRepository) : ViewModel() {

    open val serverRepository = ServerRepository()

    private val _pressureResult = MutableLiveData<PressureDto>()
    val pressureResult : LiveData<PressureDto>
        get() = _pressureResult

    fun postPressure(
        timeTag : String, maxPressure : Int,
        minPressure : Int, heartRate : Int,
        recordDate : String) = viewModelScope.launch {
        val accessToken = preferenceRepository.getApiToken().apiAccessToken
        _pressureResult.value = serverRepository.postPressure(accessToken, timeTag, maxPressure, minPressure, heartRate, recordDate)
    }
}