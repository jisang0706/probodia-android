package com.piri.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.model.ChallengeDto
import com.piri.probodia.data.remote.model.ChallengeParticipateDto
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.repository.ServerChallengeRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period

class ChallengeParticipateViewModel : BaseViewModel() {

    private val serverChallengeRepo = ServerChallengeRepository()

    private val _data = MutableLiveData<ChallengeDto>()
    val data : LiveData<ChallengeDto>
        get() = _data

    private val _untilDate = MutableLiveData<Int>()
    val untilDate : LiveData<Int>
        get() = _untilDate

    private val _date = MutableLiveData<List<Int>>()
    val date : LiveData<List<Int>>
        get() = _date

    private val _participate = MutableLiveData<ChallengeParticipateDto>()
    val participate : LiveData<ChallengeParticipateDto>
        get() = _participate

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

    fun participateChallenge(preferenceRepo : PreferenceRepository) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _participate.value = serverChallengeRepo.participateChallenge(accessToken, data.value!!.id)
        } catch (e : Exception) {
            Log.e("CHALLENGE", e.message.toString())
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _participate.value = serverChallengeRepo.participateChallenge(accessToken, data.value!!.id)
        }
    }
}