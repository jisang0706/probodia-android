package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.piri.probodia.data.remote.model.ChallengeDto
import java.time.LocalDate
import java.time.Period

class ChallengeInfoViewModel : ViewModel() {

    val _data = MutableLiveData<ChallengeDto>()
    val data : LiveData<ChallengeDto>
        get() = _data

    val _untilDate = MutableLiveData<Int>()
    val untilDate : LiveData<Int>
        get() = _untilDate

    fun setData(newData : ChallengeDto) {
        _data.value = newData

        val edDate = LocalDate.parse(newData.enDate)
        val stDate = LocalDate.parse(newData.stDate)

        _untilDate.value = Period.between(stDate, edDate).days / 7
    }
}