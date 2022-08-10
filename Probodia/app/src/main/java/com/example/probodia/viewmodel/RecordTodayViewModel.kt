package com.example.probodia.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.probodia.data.remote.model.TodayRecord
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch

class RecordTodayViewModel(application : Application) : AndroidViewModel(application) {

    private val applicationContext = getApplication<Application>().applicationContext

    private val serverRepository = ServerRepository()
    private val preferenceRecord = PreferenceRepository(applicationContext)

    private val _result = MutableLiveData<TodayRecord>()
    val result: LiveData<TodayRecord>
        get() = _result

    private var page = 1

    fun getTodayRecord() = viewModelScope.launch {
        val accessToken = preferenceRecord.getApiToken().apiAccessToken
        _result.value = serverRepository.getTodayRecords(accessToken, page++, 10)
    }

    fun reloadTodayRecord() = viewModelScope.launch {
        page = 1
        getTodayRecord()
    }
}