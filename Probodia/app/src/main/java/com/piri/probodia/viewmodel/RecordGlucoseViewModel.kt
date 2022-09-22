package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.model.GlucoseDto
import com.piri.probodia.repository.PreferenceRepository
import kotlinx.coroutines.launch

class RecordGlucoseViewModel() : BaseViewModel() {

    private val _glucoseResult = MutableLiveData<GlucoseDto.Record>()
    val glucoseResult : LiveData<GlucoseDto.Record>
        get() = _glucoseResult

    fun postGlucose(preferenceRepo : PreferenceRepository, timeTag : String, glucose : Int, recordDate : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _postGlucose(accessToken, timeTag, glucose, recordDate)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _postGlucose(accessToken, timeTag, glucose, recordDate)
        }
    }

    suspend fun _postGlucose(accessToken : String, timeTag : String, glucose : Int, recordDate : String) {
        _glucoseResult.value = serverRepository.postGlucose(accessToken, timeTag, glucose, recordDate)
    }

    fun putGlucose(preferenceRepo : PreferenceRepository, recordId : Int, timeTag : String, glucose : Int, recordDate : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _putGlucose(accessToken, recordId, timeTag, glucose, recordDate)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _putGlucose(accessToken, recordId, timeTag, glucose, recordDate)
        }
    }

    suspend fun _putGlucose(accessToken : String, recordId : Int, timeTag : String, glucose : Int, recordDate : String) {
        _glucoseResult.value = serverRepository.putGlucose(accessToken, recordId, timeTag, glucose, recordDate)
    }
}