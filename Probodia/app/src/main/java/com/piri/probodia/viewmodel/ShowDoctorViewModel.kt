package com.piri.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.body.GetRecordBody
import com.piri.probodia.data.remote.model.GlucoseDto
import com.piri.probodia.data.remote.model.RecordDatasBase
import com.piri.probodia.data.remote.model.TodayRecord
import com.piri.probodia.repository.PreferenceRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ShowDoctorViewModel : BaseViewModel() {

    private val _result = MutableLiveData<Pair<LocalDateTime, MutableList<RecordDatasBase>>>()
    val result : LiveData<Pair<LocalDateTime, MutableList<RecordDatasBase>>>
        get() = _result

    private val _localDateTime = MutableLiveData<LocalDateTime>()
    val localDateTime : LiveData<LocalDateTime>
        get() = _localDateTime

    fun setLocalDateTime(newLocalDateTime: LocalDateTime) {
        _localDateTime.value = newLocalDateTime
    }

    fun setNextLocalDateTime() {
        _localDateTime.value = _localDateTime.value?.minusMonths(1)
    }

    fun getGlucoseRecord(preferenceRepo : PreferenceRepository, dateTime : LocalDateTime) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _getRecord(accessToken, "SUGAR", dateTime)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _getRecord(accessToken, "SUGAR", dateTime)
        }
    }

    fun getPressureRecord(preferenceRepo : PreferenceRepository, dateTime : LocalDateTime) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _getRecord(accessToken, "PRESSURE", dateTime)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _getRecord(accessToken, "PRESSURE", dateTime)
        }
    }

    private suspend fun _getRecord(accessToken : String, filter : String, dateTime : LocalDateTime) {
        val endDate = "${dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00"
        val startDate = "${dateTime.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00"
        val filterType = mutableListOf(filter)
        val timeTag = mutableListOf("아침", "점심", "저녁")
        val getRecordBody = GetRecordBody(startDate, endDate, filterType, timeTag)

        val record = TodayRecord(serverRepository.getRecords(accessToken, getRecordBody))
        if (record.getDatas().isEmpty() && dateTime.year >= 2022) {
            _localDateTime.value = _localDateTime.value!!.minusMonths(1)
        } else {
            _result.value = Pair(dateTime, record.getDatas())
        }
    }
}