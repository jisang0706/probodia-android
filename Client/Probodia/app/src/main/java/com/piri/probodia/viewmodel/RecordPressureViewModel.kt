package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.model.PressureDto
import com.piri.probodia.repository.PreferenceRepository
import kotlinx.coroutines.launch

class RecordPressureViewModel() : BaseViewModel() {

    private val _pressureResult = MutableLiveData<PressureDto.Record>()
    val pressureResult : LiveData<PressureDto.Record>
        get() = _pressureResult

    fun postPressure(
        preferenceRepo : PreferenceRepository,
        timeTag : String, maxPressure : Int,
        minPressure : Int, heartRate : Int,
        recordDate : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _postPressure(
                accessToken,
                timeTag,
                maxPressure,
                minPressure,
                heartRate,
                recordDate
            )
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _postPressure(
                accessToken,
                timeTag,
                maxPressure,
                minPressure,
                heartRate,
                recordDate
            )
        }
    }

    suspend fun _postPressure(accessToken : String, timeTag : String, maxPressure : Int, minPressure: Int, heartRate: Int, recordDate: String) {
        _pressureResult.value = serverRepository.postPressure(accessToken, timeTag, maxPressure, minPressure, heartRate, recordDate)
    }

    fun putPressure(
        preferenceRepo : PreferenceRepository,
        recordId : Int,
        timeTag : String, maxPressure : Int,
        minPressure : Int, heartRate : Int,
        recordDate : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _putPressure(
                accessToken,
                recordId,
                timeTag,
                maxPressure,
                minPressure,
                heartRate,
                recordDate
            )
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _putPressure(
                accessToken,
                recordId,
                timeTag,
                maxPressure,
                minPressure,
                heartRate,
                recordDate
            )
        }
    }

    suspend fun _putPressure(accessToken : String, recordId : Int, timeTag : String, maxPressure: Int, minPressure: Int, heartRate: Int, recordDate: String) {
        _pressureResult.value = serverRepository.putPressure(accessToken, recordId, timeTag, maxPressure, minPressure, heartRate, recordDate)
    }
}