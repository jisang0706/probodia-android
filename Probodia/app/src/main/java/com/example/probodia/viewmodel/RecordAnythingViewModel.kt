package com.example.probodia.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.probodia.data.remote.model.GlucoseDto
import com.example.probodia.data.remote.model.PressureDto
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class RecordAnythingViewModel(val preferenceRepository : PreferenceRepository, val num : Int) : ViewModel() {

    private val serverRepository = ServerRepository()

    private val _glucoseResult = MutableLiveData<GlucoseDto>()
    val glucoseResult : LiveData<GlucoseDto>
        get() = _glucoseResult

    private val _pressureResult = MutableLiveData<PressureDto>()
    val pressureResult : LiveData<PressureDto>
        get() = _pressureResult

    private val _selectedTimeTag = MutableLiveData<Int>()
    val selectedTimeTag : LiveData<Int>
        get() = _selectedTimeTag

    var localDateTime : LocalDateTime = LocalDateTime.now()

    private var _buttonClickEnable = MutableLiveData<Boolean>(false)
    val buttonClickEnable : LiveData<Boolean>
        get() = _buttonClickEnable

    fun postGlucose(timeTag : String, glucose : Int, recordDate : String) = viewModelScope.launch {
        val accessToken = preferenceRepository.getApiToken().apiAccessToken
        _glucoseResult.value = serverRepository.postGlucose(accessToken, timeTag, glucose, recordDate)
    }

    fun postPressure(
        timeTag : String, maxPressure : Int,
        minPressure : Int, heartRate : Int,
        recordDate : String) = viewModelScope.launch {
        val accessToken = preferenceRepository.getApiToken().apiAccessToken
        _pressureResult.value = serverRepository.postPressure(accessToken, timeTag, maxPressure, minPressure, heartRate, recordDate)
    }

    fun setSelectedTimeTag(kind : Int) {
        Log.e("SELECTED", kind.toString())
        _selectedTimeTag.value = kind
    }

    fun setButtonClickEnable(work : Boolean) {
        _buttonClickEnable.value = work
    }
}