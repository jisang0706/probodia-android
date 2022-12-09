package com.piri.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.model.ChallengeDto
import com.piri.probodia.data.remote.model.ChallengeRecordDto
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.repository.ServerChallengeRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period

class ChallengeRecordInfoViewModel : BaseViewModel() {

    private val serverChallengeRepo = ServerChallengeRepository()

    private val _challengeInfo = MutableLiveData<ChallengeDto>()
    val challengeInfo : LiveData<ChallengeDto>
        get() = _challengeInfo

    private val _challengeRecord = MutableLiveData<MutableList<ChallengeRecordDto>>()
    val challengeRecord : LiveData<MutableList<ChallengeRecordDto>>
        get() = _challengeRecord

    private val _challengeDegree = MutableLiveData(0)
    val challengeDegree : LiveData<Int>
        get() = _challengeDegree

    private val _challengeState = MutableLiveData<String>()
    val challengeState : LiveData<String>
        get() = _challengeState

    fun setChallengeInfo(data : ChallengeDto) {
        _challengeInfo.value = data

        val edDate = LocalDate.parse(challengeInfo.value!!.enDate)

        if (Period.between(LocalDate.now(), edDate).days < 0) {
            _challengeState.value = "지급 완료"
        } else {
            _challengeState.value = "진행중"
        }
    }

    fun getChallengeRecord(preferenceRepo : PreferenceRepository, challengeId : Int) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _challengeRecord.value = serverChallengeRepo.getChallengeRecord(accessToken, challengeId)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _challengeRecord.value = serverChallengeRepo.getChallengeRecord(accessToken, challengeId)
        }

        if (challengeRecord.value != null) {
            val edDate = LocalDate.parse(challengeInfo.value!!.enDate)
            val stDate = LocalDate.parse(challengeInfo.value!!.stDate)

            _challengeDegree.value = challengeRecord.value!!.size * 100 / (Period.between(stDate, edDate).days / 7 / challengeInfo.value!!.frequency.period * challengeInfo.value!!.frequency.times)
        } else {
            _challengeDegree.value = 0
        }
    }
}