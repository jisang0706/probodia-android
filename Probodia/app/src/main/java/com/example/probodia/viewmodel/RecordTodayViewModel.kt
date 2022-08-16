package com.example.probodia.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.probodia.data.remote.body.GetRecordBody
import com.example.probodia.data.remote.model.TodayRecord
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordTodayViewModel(private val preferenceRepository : PreferenceRepository) : ViewModel() {

    private val serverRepository = ServerRepository()

    private val _result = MutableLiveData<Pair<String, MutableList<TodayRecord.AllData>>>()
    val result: LiveData<Pair<String, MutableList<TodayRecord.AllData>>>
        get() = _result

    fun getTodayRecord(timeTag : String) = viewModelScope.launch {
        val accessToken = preferenceRepository.getApiToken().apiAccessToken
        val startDate = "${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00"
        val tomorrow = LocalDateTime.now().plusDays(1)
        val endDate = "${tomorrow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00"
        val filterType = mutableListOf("SUGAR", "PRESSURE", "MEDICINE", "MEAL")
        val timeTagList = mutableListOf(timeTag)

        val getRecordBody = GetRecordBody(startDate, endDate, filterType, timeTagList)
        _result.value = Pair(timeTag, serverRepository.getRecords(accessToken, getRecordBody))
    }
}