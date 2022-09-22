package com.piri.probodia.viewmodel

import androidx.lifecycle.*
import com.piri.probodia.data.remote.body.GetRecordBody
import com.piri.probodia.data.remote.model.TodayRecord
import com.piri.probodia.repository.PreferenceRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordTodayViewModel(private val preferenceRepository : PreferenceRepository) : BaseViewModel() {

    private val _result = MutableLiveData<Pair<String, MutableList<TodayRecord.AllData>>>()
    val result: LiveData<Pair<String, MutableList<TodayRecord.AllData>>>
        get() = _result

    fun getTodayRecord(timeTag : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _getTodayRecord(accessToken, timeTag)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepository)
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _getTodayRecord(accessToken, timeTag)
        }
    }

    suspend fun _getTodayRecord(accessToken : String, timeTag : String) {
        val startDate = "${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00"
        val tomorrow = LocalDateTime.now().plusDays(1)
        val endDate = "${tomorrow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00"
        val filterType = mutableListOf("SUGAR", "PRESSURE", "MEDICINE", "MEAL")
        val timeTagList = mutableListOf(timeTag)

        val getRecordBody = GetRecordBody(startDate, endDate, filterType, timeTagList)
        _result.value = Pair(timeTag, serverRepository.getRecords(accessToken, getRecordBody))
    }
}