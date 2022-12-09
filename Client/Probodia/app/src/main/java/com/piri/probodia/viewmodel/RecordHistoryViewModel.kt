package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.body.GetRecordBody
import com.piri.probodia.data.remote.model.TodayRecord
import com.piri.probodia.repository.PreferenceRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordHistoryViewModel : BaseViewModel() {

    private val _result = MutableLiveData<Pair<Pair<LocalDateTime, String>, MutableList<TodayRecord.AllData>>>()
    val result : LiveData<Pair<Pair<LocalDateTime, String>, MutableList<TodayRecord.AllData>>>
        get() = _result

    fun getRecordHistory(preferenceRepository : PreferenceRepository, dateTime : LocalDateTime) = viewModelScope.launch(coroutineExceptionHandler) {
        val startDate = "${dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00"
        val nextDay = dateTime.plusDays(1)
        val endDate = "${nextDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00"
        val filterType = mutableListOf("SUGAR", "PRESSURE", "MEDICINE", "MEAL")

        for(i in listOf("아침", "점심", "저녁")) {
            val getRecordBody = GetRecordBody(startDate, endDate, filterType, mutableListOf(i))
            try {
                val accessToken = preferenceRepository.getApiToken().apiAccessToken
                _getRecordHistory(startDate, i, accessToken, getRecordBody)
            } catch (e : Exception) {
                refreshApiToken(preferenceRepository)
                val accessToken = preferenceRepository.getApiToken().apiAccessToken
                _getRecordHistory(startDate, i, accessToken, getRecordBody)
            }
        }
    }

    suspend fun _getRecordHistory(startDate : String, i : String, accessToken : String, getRecordBody: GetRecordBody) {
        val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        _result.value = Pair(Pair(LocalDateTime.parse(startDate, localDateTimeFormatter), i), serverRepository.getRecords(accessToken, getRecordBody))
    }
}