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

    fun setSelectedTimeTag(kind : Int) {
        _selectedTimeTag.value = kind
    }

    fun setButtonClickEnable(work : Boolean) {
        _buttonClickEnable.value = work
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