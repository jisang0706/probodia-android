package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.piri.probodia.data.remote.model.ChallengeDto
import java.time.LocalDate
import java.time.Period

class ChallengeParticipateViewModel : BaseViewModel() {

    private val _data = MutableLiveData<ChallengeDto>()
    val data : LiveData<ChallengeDto>
        get() = _data

    private val _untilDate = MutableLiveData<Int>()
    val untilDate : LiveData<Int>
        get() = _untilDate

    private val _date = MutableLiveData<List<Int>>()
    val date : LiveData<List<Int>>
        get() = _date

    fun setData(newData : ChallengeDto) {
        _data.value = newData

        val edDate = LocalDate.parse(newData.enDate)
        val stDate = LocalDate.parse(newData.stDate)

        _untilDate.value = Period.between(stDate, edDate).days / 7

        _date.value = buildList {
            add(stDate.year)
            add(stDate.monthValue)
            add(stDate.dayOfMonth)
            add(edDate.monthValue)
            add(edDate.dayOfMonth)
        }
    }
}