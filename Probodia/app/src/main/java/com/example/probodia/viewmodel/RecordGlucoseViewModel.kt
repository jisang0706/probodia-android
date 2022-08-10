package com.example.probodia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.probodia.data.remote.model.GlucoseDto
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch

class RecordGlucoseViewModel(application : Application) : AndroidViewModel(application) {

    private val applicationContext = getApplication<Application>().applicationContext

    private val serverRepository = ServerRepository()
    private val preferenceRecord = PreferenceRepository(applicationContext)

    private val _result = MutableLiveData<GlucoseDto>()
    val result : LiveData<GlucoseDto>
        get() = _result

    fun postGlucose(timeTag : String, glucose : Int, recordDate : String) = viewModelScope.launch {
        val accessToken = preferenceRecord.getApiToken().apiAccessToken
        _result.value = serverRepository.postGlucose(accessToken, timeTag, glucose, recordDate)
    }
}