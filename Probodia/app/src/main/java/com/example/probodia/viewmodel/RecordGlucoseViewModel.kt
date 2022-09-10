package com.example.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.probodia.data.remote.model.GlucoseDto
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch

class RecordGlucoseViewModel(val preferenceRepository: PreferenceRepository) : TokenViewModel() {

    private val _glucoseResult = MutableLiveData<GlucoseDto.Record>()
    val glucoseResult : LiveData<GlucoseDto.Record>
        get() = _glucoseResult

    fun postGlucose(timeTag : String, glucose : Int, recordDate : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _postGlucose(accessToken, timeTag, glucose, recordDate)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepository)
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _postGlucose(accessToken, timeTag, glucose, recordDate)
        }
    }

    suspend fun _postGlucose(accessToken : String, timeTag : String, glucose : Int, recordDate : String) {
        _glucoseResult.value = serverRepository.postGlucose(accessToken, timeTag, glucose, recordDate)
    }

    fun putGlucose(recordId : Int, timeTag : String, glucose : Int, recordDate : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _putGlucose(accessToken, recordId, timeTag, glucose, recordDate)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepository)
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _putGlucose(accessToken, recordId, timeTag, glucose, recordDate)
        }
    }

    suspend fun _putGlucose(accessToken : String, recordId : Int, timeTag : String, glucose : Int, recordDate : String) {
        _glucoseResult.value = serverRepository.putGlucose(accessToken, recordId, timeTag, glucose, recordDate)
    }
}