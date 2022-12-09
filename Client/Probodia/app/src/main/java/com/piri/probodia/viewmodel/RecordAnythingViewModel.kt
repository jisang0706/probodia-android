package com.piri.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.repository.ServerRepository
import java.time.LocalDateTime

class RecordAnythingViewModel(val num : Int) : ViewModel() {

    private val _selectedTimeTag = MutableLiveData<Int>()
    val selectedTimeTag : LiveData<Int>
        get() = _selectedTimeTag

    private val _localDateTime = MutableLiveData(LocalDateTime.now())
    val localDateTime : LiveData<LocalDateTime>
            get() = _localDateTime

    private val _buttonClickEnable = MutableLiveData(false)
    val buttonClickEnable : LiveData<Boolean>
        get() = _buttonClickEnable

    private val _isInputAll = MutableLiveData(false)
    val isInputAll : LiveData<Boolean>
        get() = _isInputAll

    private val _isServerFinish = MutableLiveData(true)
    val isServerFinish : LiveData<Boolean>
        get() = _isServerFinish

    fun setSelectedTimeTag(kind : Int) {
        _selectedTimeTag.value = kind
    }

    fun setInputAll(work : Boolean) {
        _isInputAll.value = work
        _buttonClickEnable.value = isInputAll.value!! && isServerFinish.value!!
    }

    fun setServerFinish(work : Boolean) {
        _isServerFinish.value = work
        _buttonClickEnable.value = isInputAll.value!! && isServerFinish.value!!
    }

    fun setLocalDateTime(newLocalDateTime : LocalDateTime) {
        _localDateTime.value = newLocalDateTime
    }

    fun setTime(hour : Int, minute : Int) {
        var newLocalDateTime = _localDateTime.value as LocalDateTime
        newLocalDateTime = newLocalDateTime.withHour(hour)
        newLocalDateTime = newLocalDateTime.withMinute(minute)
        _localDateTime.value = newLocalDateTime
    }

    fun setDate(year : Int, month : Int, day : Int) {
        var newLocalDateTime = _localDateTime.value as LocalDateTime
        newLocalDateTime = newLocalDateTime.withYear(year)
        newLocalDateTime = newLocalDateTime.withMonth(month)
        newLocalDateTime = newLocalDateTime.withDayOfMonth(day)
        _localDateTime.value = newLocalDateTime
    }
}