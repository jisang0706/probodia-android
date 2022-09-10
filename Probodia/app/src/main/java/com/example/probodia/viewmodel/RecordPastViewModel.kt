package com.example.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.probodia.data.remote.body.GetRecordBody
import com.example.probodia.data.remote.model.TodayRecord
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordPastViewModel : TokenViewModel() {

    private val _result = MutableLiveData<Pair<Pair<LocalDateTime, String>, MutableList<TodayRecord.AllData>>>()
    val result : LiveData<Pair<Pair<LocalDateTime, String>, MutableList<TodayRecord.AllData>>>
        get() = _result

    fun getPastRecord(preferenceRepository : PreferenceRepository, dateTime : LocalDateTime) = viewModelScope.launch(coroutineExceptionHandler) {
        val startDate = "${dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00"
        val nextDay = dateTime.plusDays(1)
        val endDate = "${nextDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00"
        val filterType = mutableListOf("SUGAR", "PRESSURE", "MEDICINE", "MEAL")
        val accessToken = preferenceRepository.getApiToken().apiAccessToken

        for(i in listOf("아침", "점심", "저녁")) {
            val getRecordBody = GetRecordBody(startDate, endDate, filterType, mutableListOf(i))
            _getPastRecord(startDate, i, accessToken, getRecordBody)
            try {
                val accessToken = preferenceRepository.getApiToken().apiAccessToken
                _getPastRecord(startDate, i, accessToken, getRecordBody)
            } catch (e : Exception) {
                refreshApiToken(preferenceRepository)
                val accessToken = preferenceRepository.getApiToken().apiAccessToken
                _getPastRecord(startDate, i, accessToken, getRecordBody)
            }
        }
    }

    suspend fun _getPastRecord(startDate : String, i : String, accessToken : String, getRecordBody: GetRecordBody) {
        val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        _result.value = Pair(Pair(LocalDateTime.parse(startDate, localDateTimeFormatter), i), serverRepository.getRecords(accessToken, getRecordBody))
    }
}