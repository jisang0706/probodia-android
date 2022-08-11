package com.example.probodia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.probodia.data.remote.model.PressureDto
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch

class RecordPressureViewModel(application : Application) : AndroidViewModel(application) {

    private val applicationContext = getApplication<Application>().applicationContext

    private val serverRepository = ServerRepository()
    private val preferenceRecord = PreferenceRepository(applicationContext)

    private val _result = MutableLiveData<PressureDto>()
    val result : LiveData<PressureDto>
        get() = _result

    fun postPressure(
        timeTag : String, maxPressure : Int,
        minPressure : Int, heartRate : Int,
        recordDate : String) = viewModelScope.launch {
        val accessToken = preferenceRecord.getApiToken().apiAccessToken
        _result.value = serverRepository.postPressure(accessToken, timeTag, maxPressure, minPressure, heartRate, recordDate)
    }
}